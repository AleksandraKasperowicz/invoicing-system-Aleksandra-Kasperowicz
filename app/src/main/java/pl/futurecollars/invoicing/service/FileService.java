package pl.futurecollars.invoicing.service;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {
  private final ObjectMapper objectMapper;

  public void writeDataToFile(String fileName, Object object) {
    log.debug("write data to file = {}", fileName);
    try {
      objectMapper.writeValue(new File(fileName), object);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public <T> List<T> getDataFromFile(String fileName, Class<T> c) {
    log.debug("get data from file = {}", fileName);
    try {
      JavaType type = objectMapper.getTypeFactory().constructParametricType(List.class, c);
      File file = new File(fileName);
      if (file.length() != 0) {
        return objectMapper.readValue(new File(fileName), type);
      }
    } catch (IOException e) {
      log.debug("problem reading data from file (" + fileName + "), will be treated as empty db, " + e);
    }
    return new ArrayList<>();
  }

  public long readLastIdFromDb(String dbPath) {
    log.debug("read id from file = {}", dbPath);
    try {
      String lastId = Files.readString(Path.of(dbPath));
      return Long.parseLong(lastId);
    } catch (Exception e) {
      return 0L;
    }
  }
}
