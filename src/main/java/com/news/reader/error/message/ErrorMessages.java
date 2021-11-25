package com.news.reader.error.message;

public enum ErrorMessages {

    IMAGE_DOWNLOAD_FAILED("ERROR001 :: IMAGE Downloading failure "),
    INVALID_NEWS_TITLE("Invalid news title. Either title be empty or null"),
    INVALID_DESCRIPTION("Invalid description. It cannot be null or empty"),
    INVALID_PUBLISHED_DATE("Invalid published date"),
    NO_NEWS_FOR_GIVEN_ID("No news for given id"),
    INTERNAL_SERVER_ERROR("Internal Server Error");

    private final String errorMessage;

    ErrorMessages(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String message() {
        return errorMessage;
    }
}
