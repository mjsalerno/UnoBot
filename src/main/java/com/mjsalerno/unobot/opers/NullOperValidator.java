package com.mjsalerno.unobot.opers;

public class NullOperValidator implements OperValidator {

	@Override
	public boolean isOper(String nick) {
		return false;
	}

}
