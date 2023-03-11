package pl.futurecollars.invoicing.ulits;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.stereotype.Service;

@Service
public class JsonService {
  private final ObjectMapper mapper;

  {
    mapper = new ObjectMapper();
    mapper.findAndRegisterModules();
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
  }

  public String convertToJson(Object o) {
    try {
      return mapper.writeValueAsString(o);
    } catch (JsonProcessingException exception) {
      throw new RuntimeException("Błąd konwertowania", exception);
    }
  }

  public <T> T convertToObject(String json, Class<T> c) {
    try {
      return mapper.readValue(json, c);
    } catch (JsonProcessingException exception) {
      throw new RuntimeException("Błąd odczytu", exception);
    }
  }
}
