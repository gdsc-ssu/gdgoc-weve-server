package com.weve.common.api.payload.code.status;

import com.google.api.Http;
import com.weve.common.api.payload.code.BaseErrorCode;
import com.weve.common.api.payload.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {
    // 일반적인 에러
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST,"COMMON400","잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"COMMON401","인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),
    _ENUM_TYPE_NOT_MATCH(HttpStatus.BAD_REQUEST, "COMMON404", "일치하는 타입이 없습니다"),

    // User
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER400", "존재하지 않는 유저 정보입니다."),
    INVALID_USER_TYPE(HttpStatus.CONFLICT, "USER401", "적절하지 않은 유저 타입입니다."),

    // Worry
    WORRY_NOT_FOUND(HttpStatus.NOT_FOUND, "WORRY400", "존재하지 않는 고민 정보입니다."),
    WORRY_ALREADY_ANSWERED(HttpStatus.CONFLICT, "WORRY401", "이미 답변이 달린 고민입니다."),
    WORRY_UNANSWERED(HttpStatus.NOT_FOUND, "WORRY402", "답변이 달리지 않은 고민입니다."),
    WORRY_NOT_MINE(HttpStatus.CONFLICT, "WORRY403", "본인이 작성한 고민이 아닙니다."),
    WORRY_APPRECIATE_NOT_FOUND(HttpStatus.NOT_FOUND, "WORRY404", "해당 고민의 답변에 대한 감사 인사가 존재하지 않습니다."),

    // Answer
    ANSWER_NOT_MINE(HttpStatus.CONFLICT, "ANSWER400", "본인이 작성한 답변이 아닙니다.");


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build();
    }
}
