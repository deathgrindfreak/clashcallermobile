package io.deathgrindfreak.util;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

public class UrlParameterContainer<K, V> {

    LinkedHashMap<K, V> paramMap;

    public UrlParameterContainer(K[] params) {
        paramMap = new LinkedHashMap<K, V>();

        for (K param : params)
            paramMap.put(param, null);
    }

    public void put(K param, V value) {
        paramMap.put(param, value);
    }

    public V get(K param) {
        return paramMap.get(param);
    }

    public String getEncodeURIString() {
        StringBuilder strb = new StringBuilder();

        try {
            for (Map.Entry<K, V> entry : paramMap.entrySet()) {
                if (entry.getValue() != null) {
                    strb.append("&");
                    strb.append(entry.getKey());
                    strb.append("=");
                    strb.append(URLEncoder.encode(entry.getValue().toString(), "UTF-8"));
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return strb.toString().substring(1);
    }
}
