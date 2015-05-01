package com.vteba.service.json.jaxrs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import org.apache.commons.io.IOUtils;

import com.alibaba.fastjson.JSON;

/**
 * 基于fastjson的jax-rx的json provider。
 * @author yinlei
 * @date 2013年10月7日 上午11:29:34
 */
@Provider
@Consumes(value = { MediaType.APPLICATION_JSON, FastJsonProvider.MIME_JAVASCRIPT, FastJsonProvider.MIME_JAVASCRIPT_MS, FastJsonProvider.JSON_EXT })
@Produces(value = { MediaType.APPLICATION_JSON })
public class FastJsonProvider implements MessageBodyReader<Object>, MessageBodyWriter<Object> {
	//private static final Logger LOGGER = LoggerFactory.getLogger(FastJsonProvider.class);
	public final static String MIME_JAVASCRIPT = "application/javascript";

    public final static String MIME_JAVASCRIPT_MS = "application/x-javascript";
    
    public final static String JSON_EXT = "*/*+json";
	
	/**
     * Looks like we need to worry about accidental
     *   data binding for types we shouldn't be handling. This is
     *   probably not a very good way to do it, but let's start by
     *   blacklisting things we are not to handle.
     *<p>
     *  (why ClassKey? since plain old Class has no hashCode() defined,
     *  lookups are painfully slow)
     */
    protected final static HashSet<ClassKey> DEFAULT_UNTOUCHABLES = new HashSet<ClassKey>();
    static {
        // First, I/O things (direct matches)
        DEFAULT_UNTOUCHABLES.add(new ClassKey(java.io.InputStream.class));
        DEFAULT_UNTOUCHABLES.add(new ClassKey(java.io.Reader.class));
        DEFAULT_UNTOUCHABLES.add(new ClassKey(java.io.OutputStream.class));
        DEFAULT_UNTOUCHABLES.add(new ClassKey(java.io.Writer.class));

        // then some primitive types
        DEFAULT_UNTOUCHABLES.add(new ClassKey(char[].class));

        /* 28-Jan-2012, tatu: 1.x excluded some additional types;
         *   but let's relax these a bit:
         */
        /* 27-Apr-2012, tatu: Ugh. As per
         *   [https://github.com/FasterXML/jackson-jaxrs-json-provider/issues/12]
         *  better revert this back, to make them untouchable again.
         */
        DEFAULT_UNTOUCHABLES.add(new ClassKey(String.class));
        DEFAULT_UNTOUCHABLES.add(new ClassKey(byte[].class));
    }

    /**
     * These are classes that we never use for reading
     * (never try to deserialize instances of these types).
     */
    public final static Class<?>[] DEFAULT_UNREADABLES = new Class<?>[] {
        InputStream.class, Reader.class
    };

    /**
     * These are classes that we never use for writing
     * (never try to serialize instances of these types).
     */
    public final static Class<?>[] DEFAULT_UNWRITABLES = new Class<?>[] {
    	InputStream.class, // as per [Issue#19]
        OutputStream.class, Writer.class,
        StreamingOutput.class, Response.class
    };
	
    /*
    /**********************************************************
    /* General configuration
    /**********************************************************
     */

    /**
     * Map that contains overrides to default list of untouchable
     * types: <code>true</code> meaning that entry is untouchable,
     * <code>false</code> that is is not.
     */
    protected HashMap<ClassKey,Boolean> cfgCustomUntouchables;

    /**
     * Whether we want to actually check that Jackson has
     * a serializer for given type. Since this should generally
     * be the case (due to auto-discovery) and since the call
     * to check availability can be bit expensive, defaults to false.
     */
    protected boolean cfgCheckCanSerialize = false;

    /**
     * Whether we want to actually check that Jackson has
     * a deserializer for given type. Since this should generally
     * be the case (due to auto-discovery) and since the call
     * to check availability can be bit expensive, defaults to false.
     */
    protected boolean cfgCheckCanDeserialize = false;

    /*
    /**********************************************************
    /* Excluded types
    /**********************************************************
     */
    public final static HashSet<ClassKey> untouchables = DEFAULT_UNTOUCHABLES;
    public final static Class<?>[] unreadableClasses = DEFAULT_UNREADABLES;
    public final static Class<?>[] unwritableClasses = DEFAULT_UNWRITABLES;
    
    /*
    /**********************************************************
    /* Configuring
    /**********************************************************
     */

    /**
     * Method for defining whether actual detection for existence of
     * a deserializer for type should be done when {@link #isReadable}
     * is called.
     */
    public void checkCanDeserialize(boolean state) {
    	cfgCheckCanDeserialize = state;
    }

    /**
     * Method for defining whether actual detection for existence of
     * a serializer for type should be done when {@link #isWriteable}
     * is called.
     */
    public void checkCanSerialize(boolean state) {
    	cfgCheckCanSerialize = state;
    }

    /**
     * Method for marking specified type as "untouchable", meaning that provider
     * will not try to read or write values of this type (or its subtypes).
     * 
     * @param type Type to consider untouchable; can be any kind of class,
     *   including abstract class or interface. No instance of this type
     *   (including subtypes, i.e. types assignable to this type) will
     *   be read or written by provider
     */
    public void addUntouchable(Class<?> type) {
        if (cfgCustomUntouchables == null) {
            cfgCustomUntouchables = new HashMap<ClassKey,Boolean>();
        }
        cfgCustomUntouchables.put(new ClassKey(type), Boolean.TRUE);
    }

    /**
     * Method for removing definition of specified type as untouchable:
     * usually only 
     * 
     * @since 2.2
     */
    public void removeUntouchable(Class<?> type) {
        if (cfgCustomUntouchables == null) {
            cfgCustomUntouchables = new HashMap<ClassKey,Boolean>();
        }
        cfgCustomUntouchables.put(new ClassKey(type), Boolean.FALSE);
    }
    
	/*
    /**********************************************************
    /* Partial MessageBodyWriter impl
    /**********************************************************
     */
	
	@Override
	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		if (!hasMatchingMediaType(mediaType)) {
            return false;
        }
        Boolean customUntouchable = findCustomUntouchable(type);
        if (customUntouchable != null) {
        	// negation: Boolean.TRUE means untouchable -> can not write
        	return !customUntouchable.booleanValue();
        }
        if (customUntouchable == null) {
            /* Ok: looks like we must weed out some core types here; ones that
             * make no sense to try to bind from JSON:
             */
            if (untouchables.contains(new ClassKey(type))) {
                return false;
            }
            // but some are interface/abstract classes, so
            for (Class<?> cls : unwritableClasses) {
                if (cls.isAssignableFrom(type)) {
                    return false;
                }
            }
        }
        // Also: if we really want to verify that we can deserialize, we'll check:
//        if (cfgCheckCanSerialize) {
//            if (!xxx.canSerialize(type)) {
//                return false;
//            }
//        }
        return true;
	}

	/**
     * Method that JAX-RS container calls to try to figure out
     * serialized length of given value. Since computation of
     * this length is about as expensive as serialization itself,
     * implementation will return -1 to denote "not known", so
     * that container will determine length from actual serialized
     * output (if needed).
     */
	@Override
	public long getSize(Object t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return -1;
	}

	@Override
	public void writeTo(Object t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
		byte[] b = JSON.toJSONBytes(t);
		entityStream.write(b);
	}

	/*
    /**********************************************************
    /* MessageBodyReader impl
    /**********************************************************
     */
	
	@Override
	public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		if (!hasMatchingMediaType(mediaType)) {
            return false;
        }

        Boolean customUntouchable = findCustomUntouchable(type);
        if (customUntouchable != null) {
        	// negation: Boolean.TRUE means untouchable -> can not write
        	return !customUntouchable.booleanValue();
        }
        if (customUntouchable == null) {
            /* Ok: looks like we must weed out some core types here; ones that
             * make no sense to try to bind from JSON:
             */
            if (untouchables.contains(new ClassKey(type))) {
                return false;
            }
            // and there are some other abstract/interface types to exclude too:
            for (Class<?> cls : unreadableClasses) {
                if (cls.isAssignableFrom(type)) {
                    return false;
                }
            }
        }
        // Finally: if we really want to verify that we can serialize, we'll check:
//        if (cfgCheckCanSerialize) {
//            if (!xxx.canSerialize(type)) {
//                return false;
//            }
//        }
        return true;
	}

	@Override
	public Object readFrom(Class<Object> type, Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException, WebApplicationException {
		byte[] bytes = IOUtils.toByteArray(entityStream);
		Object object = JSON.parseObject(bytes, genericType);
		return object;
	}

	/**
     * Helper method used to check whether given media type
     * is XML type or sub type.
     * Current implementation essentially checks to see whether
     * {@link MediaType#getSubtype} returns "xml" or something
     * ending with "+xml".
     */
    protected boolean hasMatchingMediaType(MediaType mediaType) {
    	/* As suggested by Stephen D, there are 2 ways to check: either
         * being as inclusive as possible (if subtype is "json"), or
         * exclusive (major type "application", minor type "json").
         * Let's start with inclusive one, hard to know which major
         * types we should cover aside from "application".
         */
        if (mediaType != null) {
            // Ok: there are also "xxx+json" subtypes, which count as well
            String subtype = mediaType.getSubtype();
            // [Issue#14]: also allow 'application/javascript'
           return "json".equalsIgnoreCase(subtype) || subtype.endsWith("+json")
                   || "javascript".equals(subtype)
                   // apparently Microsoft once again has interesting alternative types?
                   || "x-javascript".equals(subtype)
                   ;
        }
        /* Not sure if this can happen; but it seems reasonable
         * that we can at least produce JSON without media type?
         */
        return true;
    }
    
    protected Boolean findCustomUntouchable(Class<?> mainType) {
        if (cfgCustomUntouchables != null) {
            ClassKey key = new ClassKey(mainType);
            // First: type itself?
            Boolean b = cfgCustomUntouchables.get(key);
            if (b != null) {
                return b;
            }
            // Then supertypes (note: will not contain Object.class)
            for (Class<?> cls : findSuperTypes(mainType, null)) {
                key.reset(cls);
                b = cfgCustomUntouchables.get(key);
                if (b != null) {
                    return b;
                }
            }
        }
        return null;
    }
    
    protected static List<Class<?>> findSuperTypes(Class<?> cls, Class<?> endBefore) {
        return findSuperTypes(cls, endBefore, new ArrayList<Class<?>>(8));
    }

    protected static List<Class<?>> findSuperTypes(Class<?> cls, Class<?> endBefore, List<Class<?>> result) {
        addSuperTypes(cls, endBefore, result, false);
        return result;
    }
    
    protected static void addSuperTypes(Class<?> cls, Class<?> endBefore, Collection<Class<?>> result, boolean addClassItself) {
        if (cls == endBefore || cls == null || cls == Object.class) {
            return;
        }
        if (addClassItself) {
            if (result.contains(cls)) { // already added, no need to check supers
                return;
            }
            result.add(cls);
        }
        for (Class<?> intCls : cls.getInterfaces()) {
            addSuperTypes(intCls, endBefore, result, true);
        }
        addSuperTypes(cls.getSuperclass(), endBefore, result, true);
    }

}
