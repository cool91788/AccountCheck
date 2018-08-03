/*
 * 	AccountCheck - A BungeeCord plugin
 *	Copyright (C) 2018  Install
 *
 *   This file is part of AccountCheck source code.
 *
 *   AccountCheck is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   AccountCheck is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with AccountCheck.  If not, see <http://www.gnu.org/licenses/>.
*/

package install.java.accountcheck.util;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class IP6 {
	private final String longIP6;
	
	// 不判斷合不合法
	public String getCompressedIP6() {
		// omit leading zeros
		String compressedIP6 = longIP6.replaceAll("(^|:)(0+)([0-9a-fA-F]+)", "$1$3");
		String[] digit = compressedIP6.split(":");
		
		// 當發現有縮寫時，全展開(no leading zeros)。
		if(compressedIP6.indexOf("::") != -1) {
			StringBuilder sb = new StringBuilder();
			int maxLength = 0;
			for(int i = 0; i < digit.length; i++) {
				if(digit[i].equals("")) {
					for(int j = 0; j <= (8 - digit.length); j++) {
						if((maxLength + digit.length - i) > 8)
							break;
						sb.append("0");
						if(maxLength < 7)
							sb.append(":");
						maxLength++;
					}
				}else {
					sb.append(digit[i]);
					if(maxLength < 7)
						sb.append(":");
					maxLength++;
				}
			}
			// 若長度不足，後頭自動補0
			while(maxLength < 8) {
				sb.append("0");
				if(maxLength < 7)
					sb.append(":");
				maxLength++;
			}
			compressedIP6 = sb.toString();
			digit = compressedIP6.split(":");
		}
		
		// 省略鄰近的0
		int maxZeroCount = 0, maxZeroPos = 0;
		for(int i = 0, zeroCount = 0, zeroPos = 0; i < digit.length; i++) {
			if(digit[i].charAt(0) == '0') {
				if(zeroCount == 0)
					zeroPos = i;
				zeroCount++;
			}else {
				if(zeroCount > maxZeroCount) {
					maxZeroPos = zeroPos;
					maxZeroCount = zeroCount;
					zeroCount = 0;
				}
			}
			if((i == digit.length - 1) && (zeroCount > maxZeroCount)) {
				maxZeroPos = zeroPos;
				maxZeroCount = zeroCount;
			}
		}
		
		// 只有一個0時不縮寫。
		if(maxZeroCount <= 1)
			return compressedIP6;
		
		// 組成已壓縮的ipv6
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < digit.length; i++) {
			if(i == maxZeroPos) {
				sb.append((i == 0) ? "::" : ":");
				i += (maxZeroCount - 1);
			}else {
				sb.append(digit[i]);
				if(i < digit.length-1)
					sb.append(':');
			}
		}
		// 若開頭為"0::"，自動去第一位的0
		if(sb.charAt(0) == '0' && sb.charAt(1) == ':' && sb.charAt(2) == ':')
			sb.deleteCharAt(0);
		compressedIP6 = sb.toString();
		return compressedIP6;
	}
}
