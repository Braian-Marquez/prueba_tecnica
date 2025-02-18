package com.tsg.authentication.models.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GenericMapper {

	private ModelMapper mapper;
	private ObjectMapper jsonMapper;

	public GenericMapper(ModelMapper mapper, ObjectMapper jsonMapper) {
		super();
		this.mapper = mapper;
		this.jsonMapper = jsonMapper;
	}

	public <S, D> D jsonMap(String json, Class<D> destinationClass)
			throws JsonMappingException, JsonProcessingException {
		return jsonMapper.readValue(json, destinationClass);
	}

	public <S, D> D map(S source, Class<D> destinationClass) {
		return mapper.map(source, destinationClass);
	}

	public <S, D> List<D> mapAll(List<S> source, Class<D> destinationClass) {
		return source.stream().map(e -> map(e, destinationClass)).collect(Collectors.toList());
	}

}
