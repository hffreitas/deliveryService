package org.example.delivery.entities;

import java.time.Instant;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

/**
 * DateAudit class.
 *
 * @author hugo
 */
@Getter
@Setter
@MappedSuperclass
public abstract class DateAudit {
  @CreatedDate
  @Column(name = "createdat")
  private Instant createdAt;

  @CreatedBy
  @Column(updatable = false, name = "createdby")
  private Long createdBy;

  @LastModifiedDate
  @Column(name = "updatedat")
  private Instant updatedAt;

  @LastModifiedBy
  @Column(name = "updatedby")
  private Long updatedBy;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DateAudit dateAudit = (DateAudit) o;
    return Objects.equals(getCreatedAt(), dateAudit.getCreatedAt())
        && Objects.equals(getCreatedBy(), dateAudit.getCreatedBy())
        && Objects.equals(getUpdatedAt(), dateAudit.getUpdatedAt())
        && Objects.equals(getUpdatedBy(), dateAudit.getUpdatedBy());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getCreatedAt(), getCreatedBy(), getUpdatedAt(), getUpdatedBy());
  }
}
