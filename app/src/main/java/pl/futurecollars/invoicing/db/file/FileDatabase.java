package pl.futurecollars.invoicing.db.file;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.WithId;
import pl.futurecollars.invoicing.service.FileService;
import pl.futurecollars.invoicing.ulits.JsonService;

@Slf4j
@ConditionalOnProperty(value = "invoicing-system.database.type", havingValue = "file")
@Repository
@RequiredArgsConstructor
public class FileDatabase<T extends WithId> implements Database<T> {

  private final Path databasePath;
  private final IdProvider idProvider;
  private final FileService filesService;
  private final JsonService jsonService;
  private final Class<T> clazz;

  @Override
  public long save(T item) {
    try {
      item.setId(idProvider.getNextIdAndIncrement());
      filesService.appendLineToFile(databasePath, jsonService.convertToJson(item));

      return item.getId();
    } catch (IOException ex) {
      throw new RuntimeException("Database failed to save item", ex);
    }
  }

  @Override
  public Optional<T> getById(long id) {
    try {
      return filesService.readAllLines(databasePath)
          .stream()
          .filter(line -> containsId(line, id))
          .map(line -> jsonService.convertToObject(line, clazz))
          .findFirst();
    } catch (IOException ex) {
      throw new RuntimeException("Database failed to get item with id: " + id, ex);
    }
  }

  @Override
  public List<T> getAll() {
    try {
      return filesService.readAllLines(databasePath)
          .stream()
          .map(line -> jsonService.convertToObject(line, clazz))
          .collect(Collectors.toList());
    } catch (IOException ex) {
      throw new RuntimeException("Failed to read items from file", ex);
    }
  }

  @Override
  public Optional<T> update(long id, T updatedItem) {
    try {
      List<String> allItems = filesService.readAllLines(databasePath);
      var itemsWithoutItemWithGivenId = allItems
          .stream()
          .filter(line -> !containsId(line, id))
          .collect(Collectors.toList());

      updatedItem.setId(id);
      itemsWithoutItemWithGivenId.add(jsonService.convertToJson(updatedItem));

      filesService.writeLinesToFile(databasePath, itemsWithoutItemWithGivenId);

      allItems.removeAll(itemsWithoutItemWithGivenId);
      return allItems.isEmpty() ? Optional.empty()
          : Optional.of(jsonService.convertToObject(allItems.get(0), clazz));

    } catch (IOException ex) {
      throw new RuntimeException("Failed to update item with id: " + id, ex);
    }

  }

  @Override
  public Optional<T> delete(long id) {
    try {
      var allItems = filesService.readAllLines(databasePath);

      var itemsExceptDeleted = allItems
          .stream()
          .filter(line -> !containsId(line, id))
          .collect(Collectors.toList());

      filesService.writeLinesToFile(databasePath, itemsExceptDeleted);

      allItems.removeAll(itemsExceptDeleted);

      return allItems.isEmpty() ? Optional.empty() :
          Optional.of(jsonService.convertToObject(allItems.get(0), clazz));

    } catch (IOException ex) {
      throw new RuntimeException("Failed to delete item with id: " + id, ex);
    }
  }

  private boolean containsId(String line, long id) {
    return line.contains("{\"id\":" + id + ",");
  }
}