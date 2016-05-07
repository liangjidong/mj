package org.chudian.mj.socket;

import org.springframework.util.StringUtils;

public class StringTest {
	public static void main(String args[]){
		String[] s=StringUtils.delimitedListToStringArray("aaojojouo.jpeg", ".");
		String ss="aadsafda.jpeg";
		int a=ss.indexOf(".");
		System.out.println(a);
		System.out.println(ss.substring(0,a));
		for(String t:s){
			System.out.println(t);
		}
	}

}
