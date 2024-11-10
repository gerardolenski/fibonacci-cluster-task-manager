package org.gol.taskmanager.outbox.infrastructure.db;

import org.gol.taskmanager.outbox.domain.model.MessageBody;
import org.gol.taskmanager.outbox.domain.model.MessageHeader;
import org.gol.taskmanager.outbox.domain.model.MessageId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import static jakarta.persistence.FetchType.EAGER;
import static java.util.Optional.ofNullable;

@Getter
@Setter
@ToString
@Entity(name = "OutboxMsg")
@Table(name = "outbox", indexes = {
        @Index(name = "om_is_delivered_idx", columnList = "is_delivered, created_at")
})
@EntityListeners(AuditingEntityListener.class)
class OutboxMsgEntity {

    @Id
    @Column(name = "id")
    private UUID id;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = EAGER)
    @JoinColumn(name = "message_id")
    private List<OutboxMsgHeader> headers;

    @Column(name = "data", columnDefinition = "TEXT", nullable = false)
    private String data;

    @Column(name = "is_delivered", nullable = false)
    private Boolean isDelivered;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    static OutboxMsgEntity createOutboxMsg(MessageId messageId, MessageHeader header, MessageBody body) {
        OutboxMsgEntity entity = new OutboxMsgEntity();
        entity.setId(messageId.value());
        entity.headers = ofNullable(header)
                .map(OutboxMsgHeader::of)
                .map(List::of)
                .orElseGet(List::of);
        entity.setData(body.value());
        entity.setIsDelivered(false);
        return entity;
    }
}
