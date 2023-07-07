package com.example.leonproject.dao.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "punch_clock")
public class TimeSheetDO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "work_date")
    private LocalDateTime workDate;

    @Column(name = "time_diff")
    private LocalDateTime timeDiff;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private AccountDO accountDO;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getWorkDate() {
        return workDate;
    }

    public void setWorkDate(LocalDateTime workDate) {
        this.workDate = workDate;
    }

    public LocalDateTime getTimeDiff() {
        return timeDiff;
    }

    public void setTimeDiff(LocalDateTime timeDiff) {
        this.timeDiff = timeDiff;
    }

    public AccountDO getAccountDO() {
        return accountDO;
    }

    public void setAccountDO(AccountDO accountDO) {
        this.accountDO = accountDO;
    }
}
