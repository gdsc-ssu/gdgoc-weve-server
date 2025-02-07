package com.weve.common.api.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.weve.common.api.payload.code.BaseCode;
import com.weve.common.api.payload.code.status.SuccessStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"isSuccess", "code", "message", "result"})
public class BasicResponse<T> {

	@JsonProperty
	private final Boolean isSuccess;
	private final String code;
	private final String message;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private T result;

	// 성공한 경우 응답 생성
	public static <T> BasicResponse<T> onSuccess(T result){
		return new BasicResponse<>(true, SuccessStatus._OK.getCode() , SuccessStatus._OK.getMessage(), result);
	}

	public static <T> BasicResponse<T> of(BaseCode code, T result){
		return new BasicResponse<>(true, code.getReasonHttpStatus().getCode() , code.getReasonHttpStatus().getMessage(), result);
	}

	// 실패한 경우
	public static <T> BasicResponse<T> onFailure(String code, String message, T data) {
		return new BasicResponse<>(false, code, message, data);
	}
}