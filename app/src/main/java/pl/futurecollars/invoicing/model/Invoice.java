package pl.futurecollars.invoicing.model;

import static javax.persistence.CascadeType.ALL;

import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Builder
@AllArgsConstructor

public class Invoice implements WithId {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @ApiModelProperty(value = "Invoice ID ", required = true, example = "1")
  private Long id;
  @ApiModelProperty(value = "Invoice number (assigned by user)", required = true, example = "2020/03/08/0000001")
  private String number;
  @ApiModelProperty(value = "operation date", example = "2023-03-18")
  private LocalDate date;

  @JoinColumn(name = "buyer")
  @OneToOne(cascade = ALL)
  @ApiModelProperty(value = "company who bought", required = true)
  private Company buyer;

  @JoinColumn(name = "seller")
  @OneToOne(cascade = ALL)
  @ApiModelProperty(value = "company who is selling", required = true)
  private Company seller;

  @JoinTable(name = "invoice_invoice_entry", inverseJoinColumns = @JoinColumn(name = "invoice_entry_id"))
  @OneToMany(cascade = ALL, orphanRemoval = true, fetch = FetchType.EAGER)
  @ApiModelProperty(value = "list of products", required = true)
  private List<InvoiceEntry> entries;
}
