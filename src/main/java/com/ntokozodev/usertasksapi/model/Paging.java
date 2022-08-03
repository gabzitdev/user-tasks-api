package com.ntokozodev.usertasksapi.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Paging {
    private int pageNumber;
    private int pageSize;
    private int totalPages;
    private long totalElements;
    private boolean isLast;
}
