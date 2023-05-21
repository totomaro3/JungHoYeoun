package com.example.demo.vo;

import lombok.Data;
import lombok.Getter;

@Data
public class ResultData<DT> {
	@Getter
	private String resultCode;
	@Getter
	private String msg;
	@Getter
	private String data1Name;
	@Getter
	private DT data1;

	public static <DT> ResultData<DT> from(String resultCode, String msg) {
		// TODO Auto-generated method stub
		return from(resultCode, msg, null, null);
	}
	
	public static <DT> ResultData<DT> from(String resultCode, String msg, String data1Name, DT data1) {
		ResultData<DT> rd = new ResultData<DT>();
		rd.resultCode = resultCode;
		rd.msg = msg;
		rd.data1Name = data1Name;
		rd.data1 = data1;

		return rd;
	}

	public boolean isSuccess() {
		return resultCode.startsWith("S-");
	}

	public boolean isFail() {
		return isSuccess() == false;
	}

	public static <DT> ResultData<DT> newData(ResultData<?> rd, String data1Name, DT data1) {
		
		return ResultData.from(rd.getResultCode(), rd.getMsg(), data1Name, data1);
	}
	
}