package com.mkyong.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author Mohamed Atallah
 */
@Entity
@Table(name = "KNOWLEDGEPOOL2")
public class Knowledgepool implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "POOLID")
    private String poolid;
    @Column(name = "NAME")
    private String name;
    @Column(name = "CREATOR")
    private long creator;
    @Column(name = "CREATIONDATE")
    private long creationdate;
    @Column(name = "KNOWLEDGEPOOL_TYPE")
    private Short knowledgepoolType;
    @Column(name = "DOMAIN_ID")
    private Integer domainId;

    public Knowledgepool() {
    }

    public Knowledgepool(String poolid) {
        this.poolid = poolid;
    }

    public Knowledgepool(String poolid, String name, long creator, long creationdate) {
        this.poolid = poolid;
        this.name = name;
        this.creator = creator;
        this.creationdate = creationdate;
    }

    public String getPoolid() {
        return poolid;
    }

    public void setPoolid(String poolid) {
        this.poolid = poolid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCreator() {
        return creator;
    }

    public void setCreator(long creator) {
        this.creator = creator;
    }

    public long getCreationdate() {
        return creationdate;
    }

    public void setCreationdate(long creationdate) {
        this.creationdate = creationdate;
    }

    public Short getKnowledgepoolType() {
        return knowledgepoolType;
    }

    public void setKnowledgepoolType(Short knowledgepoolType) {
        this.knowledgepoolType = knowledgepoolType;
    }

    public Integer getDomainId() {
        return domainId;
    }

    public void setDomainId(Integer domainId) {
        this.domainId = domainId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (poolid != null ? poolid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Knowledgepool)) {
            return false;
        }
        Knowledgepool other = (Knowledgepool) object;
        if ((this.poolid == null && other.poolid != null) || (this.poolid != null && !this.poolid.equals(other.poolid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.en.Knowledgepool[ poolid=" + poolid + " ]";
    }
}
