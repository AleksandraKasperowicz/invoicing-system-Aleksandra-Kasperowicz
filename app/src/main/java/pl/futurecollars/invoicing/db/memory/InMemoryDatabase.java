package pl.futurecollars.invoicing.db.memory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.WithId;

@Slf4j
@ConditionalOnProperty(value = "invoicing-system.database.type", havingValue = "inmemory")
@Repository
public class InMemoryDatabase<T extends WithId> implements Database<T> {

  private final Map<Long, T> items = new HashMap<>();
  private long currentId = 0;

  @Override
  public long save(T item) {
    item.setId(getNextId());
    log.info("save invoice id = {}", item.getId());
    items.put(item.getId(), item);
    log.debug("after DB update invoices.size = {}", items.size());
    return item.getId();

  }

  @Override
  public Optional<T> getById(long id) {
    log.debug("getById(id = {})", id);
    return Optional.ofNullable(items.get(id));
  }

  @Override
  public List<T> getAll() {
    log.info("getAll");
    List<T> allInvoices = new ArrayList<>(items.values());
    log.debug("allInvoices.size = {}", allInvoices.size());
    return allInvoices;
  }

  @Override
  public Optional<T> update(long id, T updatedItem) {
    log.info("update invoice id = {}", id);
    updatedItem.setId(id);

    return Optional.ofNullable(items.put(id, updatedItem));
  }

  @Override
  public Optional<T> delete(long id) {
    log.info("delete invoice id = {}", id);
    log.debug("invoice id = {} deleted", id);
    return Optional.ofNullable(items.remove(id));

  }

  private long getNextId() {
    return ++currentId;
  }
}
