package com.covilla.vo;

import com.covilla.util.ValidatorUtil;

/**
 * @description 通讯协议
 *
 * @author xuys
 * 
 * @time 2015年7月6日 下午3:25:01
 **/
public class ResultMsg {
	public static final String FAIL = "FAIL";
	public static final String SUCCESS = "SUCCESS";
	
	/** 协议层 **/
	/*private String returnCode = SUCCESS;
	
	private String returnMsg;*/
	
	/** 业务层   **/
	private String resultCode = FAIL;
	
    private String errCode;
    
    private String errCodeDesc;

    private Object result;
    
    public ResultMsg(){}
    
    /**
     * @description 构造通讯协议
     *
     * @author xuys
     * 
     * @time 2015年7月22日 上午11:04:29
     *
     * @param
     *
     */
    public static ResultMsg buildOpeMsg(ResultMsg opeMsg, String resultCode, String errorCode, String errorDesc){
    	if (ValidatorUtil.isNull(opeMsg)) {
			opeMsg = new ResultMsg();
		}
    	opeMsg.setResultCode(resultCode);
    	opeMsg.setErrCode(errorCode);
    	opeMsg.setErrCodeDesc(errorDesc);
    	
    	return opeMsg;
    }

	/**
	 * 构造业务失败协议
	 * @param errCode
	 * @param errCodeDesc
     * @return
     */
	public static ResultMsg buildFailMsg(String errCode, String errCodeDesc){
		ResultMsg resultMsg = new ResultMsg();
		resultMsg.setErrCode(errCode);
		resultMsg.setErrCodeDesc(errCodeDesc);
		return resultMsg;
	}

	/**
	 * 构造业务成功协议
	 * @return
     */
	public static ResultMsg buildSuccessMsg(){
		ResultMsg resultMsg = new ResultMsg();
		resultMsg.setResultCode(SUCCESS);
		return resultMsg;
	}
	public static ResultMsg buildSuccessMsg(Object result){
		ResultMsg resultMsg = new ResultMsg();
		resultMsg.setResultCode(SUCCESS);
		resultMsg.setResult(result);
		return resultMsg;
	}

	/*public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public String getReturnMsg() {
		return returnMsg;
	}

	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}*/

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	@SuppressWarnings("rawtypes")
	public Object getResult() {
		return result;
	}

	@SuppressWarnings("rawtypes")
	public void setResult(Object result) {
		this.result = result;
	}

	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	public String getErrCodeDesc() {
		return errCodeDesc;
	}

	public void setErrCodeDesc(String errCodeDesc) {
		this.errCodeDesc = errCodeDesc;
	}

}
