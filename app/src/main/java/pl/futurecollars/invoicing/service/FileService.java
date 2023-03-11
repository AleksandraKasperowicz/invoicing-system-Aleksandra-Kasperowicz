package pl.futurecollars.invoicing.service;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FileService {
  private final ObjectMapper objectMapper;

  public FileService() {
    this.objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
  }

  public void writeDataToFile(String fileName, Object object) {
    try {
      objectMapper.writeValue(new File(fileName), object);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public <T> List<T> getDataFromFile(String fileName, Class<T> c) {
    try {
      JavaType type = objectMapper.getTypeFactory().constructParametricType(List.class, c);
      File file = new File(fileName);
      if (file.length() != 0) {
        return objectMapper.readValue(new File(fileName), type);
      }
    } catch (IOException e) {
      System.out.println("problem reading data from file (" + fileName + "), will be treated as empty db, " + e);
    }
    return new ArrayList<>();
  }

  public long readLastIdFromDb(String dbPath) {
    try {
      String lastId = Files.readString(Path.of(dbPath));
      return Long.parseLong(lastId);
    } catch (Exception e) {
      return 0L;
    }
  }
}
