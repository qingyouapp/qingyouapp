package com.xwq.qingyouapp.command;


public class Command {

	public enum RESULT_TYPE {
		Concise, Detailed
	};

	private String cliCmd;
	private String cliAction;
	private RESULT_TYPE resultType;
	private String parameter;

	public Command(String cliCmd, String cliAction, RESULT_TYPE resultType) {
		this.cliCmd = cliCmd;
		this.cliAction = cliAction;
		this.resultType = resultType;
	}
	
	public Command(String cliCmd, String cliAction, RESULT_TYPE resultType, String parameter) {
		this.cliCmd = cliCmd;
		this.cliAction = cliAction;
		this.resultType = resultType;
		this.parameter = parameter;
	}

	public String getCliCmd() {
		return cliCmd;
	}

	public void setCliCmd(String cliCmd) {
		this.cliCmd = cliCmd;
	}

	public String getCliAction() {
		return cliAction;
	}

	public void setCliAction(String cliAction) {
		this.cliAction = cliAction;
	}

	public RESULT_TYPE getResultType() {
		return resultType;
	}

	public void setResultType(RESULT_TYPE resultType) {
		this.resultType = resultType;
	}

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	
}
