package com.example.leonproject.dao.entity;

import jakarta.persistence.*;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "time_sheet")
public class TimeSheetDO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "work_date")
    private LocalDate workDate;

    @Column(name = "time_diff")
    private Time timeDiff;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private AccountDO accountDO;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getWorkDate() {
        return workDate;
    }

    public void setWorkDate(LocalDate workDate) {
        this.workDate = workDate;
    }

    public Time getTimeDiff() {
        return timeDiff;
    }

    public void setTimeDiff(Time timeDiff) {
        this.timeDiff = timeDiff;
    }

    public AccountDO getAccountDO() {
        return accountDO;
    }

    public void setAccountDO(AccountDO accountDO) {
        this.accountDO = accountDO;
    }
}
