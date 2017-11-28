package com.mjsalerno.unobot.opers;

import java.util.HashSet;
import java.util.Set;

import com.google.common.base.Splitter;

public class SimpleOperValidator implements OperValidator {
	
	private Set<String> validOpers = new HashSet<>();
	
	public SimpleOperValidator(String opers) {
		Iterable<String> oper = Splitter.on(',').trimResults().omitEmptyStrings().split(opers);
		for (String o : oper) {
			validOpers.add( o.toLowerCase() );
		}
	}

	@Override
	public boolean isOper(String nick) {
		return validOpers.contains( nick.toLowerCase() );
	}

}
