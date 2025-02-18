package com.tsg.commons.models.enums;

import org.springframework.data.domain.Sort;
import lombok.Data;
import java.io.Serializable;
@Data
public class GenericPage implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private int pageNumber = 0;
    private int pageSize = 10;
    private Sort.Direction sortDirection = Sort.Direction.ASC;
    private String sortBy = "id";
    

    
}