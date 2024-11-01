package org.gol.taskmanager.outbox.infrastructure.db;


import org.gol.taskmanager.outbox.domain.model.MessageHeader;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity(name = "OutboxMsgHeader")
@Table(name = "outbox_header")
class OutboxMsgHeader {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "hdr_key", length = 50)
    private String key;

    @Column(name = "hdr_value", length = 1024)
    private String value;

    static OutboxMsgHeader of(MessageHeader header) {
        OutboxMsgHeader outboxMsgHeader = new OutboxMsgHeader();
        outboxMsgHeader.setKey(header.key());
        outboxMsgHeader.setValue(header.value());
        return outboxMsgHeader;
    }
}
