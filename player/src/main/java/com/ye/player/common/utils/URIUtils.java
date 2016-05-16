package com.ye.player.common.utils;

import java.util.Map;

public class URIUtils {

	public static String appendParams(String targetUri, Map<String, String> paramMap) throws Exception {
		if (paramMap == null || paramMap.isEmpty()) {
			return targetUri;
		}
		int paramIndex = targetUri.indexOf('?');
		int hashStartIndex = targetUri.indexOf('#');
		String serverPath = targetUri;
		String params = "";
		String hash = "";
		if (paramIndex >= 0) {
			serverPath = targetUri.substring(0, paramIndex);
			if (hashStartIndex >= 0) {
				params = targetUri.substring(paramIndex + 1, hashStartIndex);
				hash = targetUri.substring(hashStartIndex + 1);
			} else {
				params = targetUri.substring(paramIndex + 1);
			}
		} else if (hashStartIndex >= 0) {
			serverPath = targetUri.substring(0, hashStartIndex);
			hash = targetUri.substring(hashStartIndex + 1);
		}
		StringBuffer resultBuffer = new StringBuffer();
		if (!StringUtil.isEmpty(serverPath)) {
			resultBuffer.append(serverPath);
		}
		resultBuffer.append("?");
		if (!StringUtil.isEmpty(params)) {
			resultBuffer.append(params);
		}
		if (!StringUtil.isEmpty(params)) {
			resultBuffer.append("&");
		}
		int index = 0;
		for (Map.Entry<String, String> entry : paramMap.entrySet()) {
			if (index != 0) {
				resultBuffer.append("&");
			}
			resultBuffer.append(entry.getKey()).append("=").append(entry.getValue());
			index++;
		}
		if (!StringUtil.isEmpty(hash)) {
			resultBuffer.append("#").append(hash);
		}
		return resultBuffer.toString();
	}

	public static String appendHttpPrefix(String targetUri) {
		String resultUrl = null;
		if (targetUri.startsWith("http://") || targetUri.startsWith("https://")) {
			resultUrl = targetUri;
		} else {
			resultUrl = "http://" + targetUri;
		}
		return resultUrl;
	}
}
