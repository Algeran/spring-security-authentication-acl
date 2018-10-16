package com.springfreamwork.springsecurity.app.configurations;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.security.acls.domain.AccessControlEntryImpl;
import org.springframework.security.acls.domain.AclAuthorizationStrategy;
import org.springframework.security.acls.domain.AclImpl;
import org.springframework.security.acls.domain.AuditLogger;
import org.springframework.security.acls.domain.DefaultPermissionFactory;
import org.springframework.security.acls.domain.DefaultPermissionGrantingStrategy;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PermissionFactory;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.jdbc.LookupStrategy;
import org.springframework.security.acls.model.AccessControlEntry;
import org.springframework.security.acls.model.Acl;
import org.springframework.security.acls.model.AclCache;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.PermissionGrantingStrategy;
import org.springframework.security.acls.model.Sid;
import org.springframework.security.acls.model.UnloadedSidException;
import org.springframework.security.util.FieldUtils;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MyLookupStrategy implements LookupStrategy {

    private static final String DEFAULT_SELECT_CLAUSE_COLUMNS = "select acl_object_identity.object_id_identity, acl_entry.ace_order,  acl_object_identity.id as acl_id, acl_object_identity.parent_object, acl_object_identity.entries_inheriting, acl_entry.id as ace_id, acl_entry.mask,  acl_entry.granting,  acl_entry.audit_success, acl_entry.audit_failure,  acl_sid.principal as ace_principal, acl_sid.sid as ace_sid,  acli_sid.principal as acl_principal, acli_sid.sid as acl_sid, acl_class.class ";
    private static final String DEFAULT_SELECT_CLAUSE_ACL_CLASS_ID_TYPE_COLUMN = ", acl_class.class_id_type  ";
    private static final String DEFAULT_SELECT_CLAUSE_FROM = "from acl_object_identity left join acl_sid acli_sid on acli_sid.id = acl_object_identity.owner_sid left join acl_class on acl_class.id = acl_object_identity.object_id_class   left join acl_entry on acl_object_identity.id = acl_entry.acl_object_identity left join acl_sid on acl_entry.sid = acl_sid.id  where ( ";
    public static final String DEFAULT_SELECT_CLAUSE = "select acl_object_identity.object_id_identity, acl_entry.ace_order,  acl_object_identity.id as acl_id, acl_object_identity.parent_object, acl_object_identity.entries_inheriting, acl_entry.id as ace_id, acl_entry.mask,  acl_entry.granting,  acl_entry.audit_success, acl_entry.audit_failure,  acl_sid.principal as ace_principal, acl_sid.sid as ace_sid,  acli_sid.principal as acl_principal, acli_sid.sid as acl_sid, acl_class.class from acl_object_identity left join acl_sid acli_sid on acli_sid.id = acl_object_identity.owner_sid left join acl_class on acl_class.id = acl_object_identity.object_id_class   left join acl_entry on acl_object_identity.id = acl_entry.acl_object_identity left join acl_sid on acl_entry.sid = acl_sid.id  where ( ";
    public static final String DEFAULT_ACL_CLASS_ID_SELECT_CLAUSE = "select acl_object_identity.object_id_identity, acl_entry.ace_order,  acl_object_identity.id as acl_id, acl_object_identity.parent_object, acl_object_identity.entries_inheriting, acl_entry.id as ace_id, acl_entry.mask,  acl_entry.granting,  acl_entry.audit_success, acl_entry.audit_failure,  acl_sid.principal as ace_principal, acl_sid.sid as ace_sid,  acli_sid.principal as acl_principal, acli_sid.sid as acl_sid, acl_class.class , acl_class.class_id_type  from acl_object_identity left join acl_sid acli_sid on acli_sid.id = acl_object_identity.owner_sid left join acl_class on acl_class.id = acl_object_identity.object_id_class   left join acl_entry on acl_object_identity.id = acl_entry.acl_object_identity left join acl_sid on acl_entry.sid = acl_sid.id  where ( ";
    private static final String DEFAULT_LOOKUP_KEYS_WHERE_CLAUSE = "(acl_object_identity.id = ?)";
    private static final String DEFAULT_LOOKUP_IDENTITIES_WHERE_CLAUSE = "(acl_object_identity.object_id_identity = ? and acl_class.class = ?)";
    public static final String DEFAULT_ORDER_BY_CLAUSE = ") order by acl_object_identity.object_id_identity asc, acl_entry.ace_order asc";
    private final AclAuthorizationStrategy aclAuthorizationStrategy;
    private PermissionFactory permissionFactory;
    private final AclCache aclCache;
    private final PermissionGrantingStrategy grantingStrategy;
    private final JdbcTemplate jdbcTemplate;
    private int batchSize;
    private final Field fieldAces;
    private final Field fieldAcl;
    private String selectClause;
    private String lookupPrimaryKeysWhereClause;
    private String lookupObjectIdentitiesWhereClause;
    private String orderByClause;

    public MyLookupStrategy(DataSource dataSource, AclCache aclCache, AclAuthorizationStrategy aclAuthorizationStrategy, AuditLogger auditLogger) {
        this(dataSource, aclCache, aclAuthorizationStrategy, (PermissionGrantingStrategy)(new DefaultPermissionGrantingStrategy(auditLogger)));
    }

    public MyLookupStrategy(DataSource dataSource, AclCache aclCache, AclAuthorizationStrategy aclAuthorizationStrategy, PermissionGrantingStrategy grantingStrategy) {
        this.permissionFactory = new DefaultPermissionFactory();
        this.batchSize = 50;
        this.fieldAces = FieldUtils.getField(AclImpl.class, "aces");
        this.fieldAcl = FieldUtils.getField(AccessControlEntryImpl.class, "acl");
        this.selectClause = "select acl_object_identity.object_id_identity, acl_entry.ace_order,  acl_object_identity.id as acl_id, acl_object_identity.parent_object, acl_object_identity.entries_inheriting, acl_entry.id as ace_id, acl_entry.mask,  acl_entry.granting,  acl_entry.audit_success, acl_entry.audit_failure,  acl_sid.principal as ace_principal, acl_sid.sid as ace_sid,  acli_sid.principal as acl_principal, acli_sid.sid as acl_sid, acl_class.class from acl_object_identity left join acl_sid acli_sid on acli_sid.id = acl_object_identity.owner_sid left join acl_class on acl_class.id = acl_object_identity.object_id_class   left join acl_entry on acl_object_identity.id = acl_entry.acl_object_identity left join acl_sid on acl_entry.sid = acl_sid.id  where ( ";
        this.lookupPrimaryKeysWhereClause = "(acl_object_identity.id = ?)";
        this.lookupObjectIdentitiesWhereClause = "(acl_object_identity.object_id_identity = ? and acl_class.class = ?)";
        this.orderByClause = ") order by acl_object_identity.object_id_identity asc, acl_entry.ace_order asc";
        Assert.notNull(dataSource, "DataSource required");
        Assert.notNull(aclCache, "AclCache required");
        Assert.notNull(aclAuthorizationStrategy, "AclAuthorizationStrategy required");
        Assert.notNull(grantingStrategy, "grantingStrategy required");
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.aclCache = aclCache;
        this.aclAuthorizationStrategy = aclAuthorizationStrategy;
        this.grantingStrategy = grantingStrategy;
        this.fieldAces.setAccessible(true);
        this.fieldAcl.setAccessible(true);
    }

    private String computeRepeatingSql(String repeatingSql, int requiredRepetitions) {
        assert requiredRepetitions > 0 : "requiredRepetitions must be > 0";

        String startSql = this.selectClause;
        String endSql = this.orderByClause;
        StringBuilder sqlStringBldr = new StringBuilder(startSql.length() + endSql.length() + requiredRepetitions * (repeatingSql.length() + 4));
        sqlStringBldr.append(startSql);

        for(int i = 1; i <= requiredRepetitions; ++i) {
            sqlStringBldr.append(repeatingSql);
            if (i != requiredRepetitions) {
                sqlStringBldr.append(" or ");
            }
        }

        sqlStringBldr.append(endSql);
        return sqlStringBldr.toString();
    }

    private List<AccessControlEntryImpl> readAces(AclImpl acl) {
        try {
            return (List)this.fieldAces.get(acl);
        } catch (IllegalAccessException var3) {
            throw new IllegalStateException("Could not obtain AclImpl.aces field", var3);
        }
    }

    private void setAclOnAce(AccessControlEntryImpl ace, AclImpl acl) {
        try {
            this.fieldAcl.set(ace, acl);
        } catch (IllegalAccessException var4) {
            throw new IllegalStateException("Could not or set AclImpl on AccessControlEntryImpl fields", var4);
        }
    }

    private void setAces(AclImpl acl, List<AccessControlEntryImpl> aces) {
        try {
            this.fieldAces.set(acl, aces);
        } catch (IllegalAccessException var4) {
            throw new IllegalStateException("Could not set AclImpl entries", var4);
        }
    }

    private void lookupPrimaryKeys(Map<Serializable, Acl> acls, final Set<Long> findNow, List<Sid> sids) {
        Assert.notNull(acls, "ACLs are required");
        Assert.notEmpty(findNow, "Items to find now required");
        String sql = this.computeRepeatingSql(this.lookupPrimaryKeysWhereClause, findNow.size());
        Set<Long> parentsToLookup = (Set)this.jdbcTemplate.query(sql, new PreparedStatementSetter() {
            public void setValues(PreparedStatement ps) throws SQLException {
                int i = 0;
                Iterator var3 = findNow.iterator();

                while(var3.hasNext()) {
                    Long toFind = (Long)var3.next();
                    ++i;
                    ps.setLong(i, toFind);
                }

            }
        }, new MyLookupStrategy.ProcessResultSet(acls, sids));
        if (parentsToLookup.size() > 0) {
            this.lookupPrimaryKeys(acls, parentsToLookup, sids);
        }

    }

    public final Map<ObjectIdentity, Acl> readAclsById(List<ObjectIdentity> objects, List<Sid> sids) {
        Assert.isTrue(this.batchSize >= 1, "BatchSize must be >= 1");
        Assert.notEmpty(objects, "Objects to lookup required");
        Map<ObjectIdentity, Acl> result = new HashMap();
        Set<ObjectIdentity> currentBatchToLoad = new HashSet();

        for(int i = 0; i < objects.size(); ++i) {
            ObjectIdentity oid = (ObjectIdentity)objects.get(i);
            boolean aclFound = false;
            if (result.containsKey(oid)) {
                aclFound = true;
            }

            if (!aclFound) {
                Acl acl = this.aclCache.getFromCache(oid);
                if (acl != null) {
                    if (!acl.isSidLoaded(sids)) {
                        throw new IllegalStateException("Error: SID-filtered element detected when implementation does not perform SID filtering - have you added something to the cache manually?");
                    }

                    result.put(acl.getObjectIdentity(), acl);
                    aclFound = true;
                }
            }

            if (!aclFound) {
                currentBatchToLoad.add(oid);
            }

            if ((currentBatchToLoad.size() == this.batchSize || i + 1 == objects.size()) && currentBatchToLoad.size() > 0) {
                Map<ObjectIdentity, Acl> loadedBatch = this.lookupObjectIdentities(currentBatchToLoad, sids);
                result.putAll(loadedBatch);
                Iterator var9 = loadedBatch.values().iterator();

                while(var9.hasNext()) {
                    Acl loadedAcl = (Acl)var9.next();
                    this.aclCache.putInCache((AclImpl)loadedAcl);
                }

                currentBatchToLoad.clear();
            }
        }

        return result;
    }

    private Map<ObjectIdentity, Acl> lookupObjectIdentities(final Collection<ObjectIdentity> objectIdentities, List<Sid> sids) {
        Assert.notEmpty(objectIdentities, "Must provide identities to lookup");
        Map<Serializable, Acl> acls = new HashMap();
        String sql = this.computeRepeatingSql(this.lookupObjectIdentitiesWhereClause, objectIdentities.size());
        Set<Long> parentsToLookup = (Set)this.jdbcTemplate.query(sql, new PreparedStatementSetter() {
            public void setValues(PreparedStatement ps) throws SQLException {
                int i = 0;

                for(Iterator var3 = objectIdentities.iterator(); var3.hasNext(); ++i) {
                    ObjectIdentity oid = (ObjectIdentity)var3.next();
                    String type = oid.getType();
                    String identifier = oid.getIdentifier().toString();
                    ps.setString(2 * i + 1, identifier);
                    ps.setString(2 * i + 2, type);
                }

            }
        }, new MyLookupStrategy.ProcessResultSet(acls, sids));
        if (parentsToLookup.size() > 0) {
            this.lookupPrimaryKeys(acls, parentsToLookup, sids);
        }

        Map<ObjectIdentity, Acl> resultMap = new HashMap();
        Iterator var7 = acls.values().iterator();

        while(var7.hasNext()) {
            Acl inputAcl = (Acl)var7.next();
            Assert.isInstanceOf(AclImpl.class, inputAcl, "Map should have contained an AclImpl");
            Assert.isInstanceOf(Long.class, ((AclImpl)inputAcl).getId(), "Acl.getId() must be Long");
            Acl result = this.convert(acls, (Long)((AclImpl)inputAcl).getId());
            resultMap.put(result.getObjectIdentity(), result);
        }

        return resultMap;
    }

    private AclImpl convert(Map<Serializable, Acl> inputMap, Long currentIdentity) {
        Assert.notEmpty(inputMap, "InputMap required");
        Assert.notNull(currentIdentity, "CurrentIdentity required");
        Acl uncastAcl = (Acl)inputMap.get(currentIdentity);
        Assert.isInstanceOf(AclImpl.class, uncastAcl, "The inputMap contained a non-AclImpl");
        AclImpl inputAcl = (AclImpl)uncastAcl;
        Acl parent = inputAcl.getParentAcl();
        if (parent != null && parent instanceof MyLookupStrategy.StubAclParent) {
            MyLookupStrategy.StubAclParent stubAclParent = (MyLookupStrategy.StubAclParent)parent;
            parent = this.convert(inputMap, stubAclParent.getId());
        }

        AclImpl result = new AclImpl(inputAcl.getObjectIdentity(), (Long)inputAcl.getId(), this.aclAuthorizationStrategy, this.grantingStrategy, (Acl)parent, (List)null, inputAcl.isEntriesInheriting(), inputAcl.getOwner());
        List<AccessControlEntryImpl> aces = this.readAces(inputAcl);
        List<AccessControlEntryImpl> acesNew = new ArrayList();
        Iterator var9 = aces.iterator();

        while(var9.hasNext()) {
            AccessControlEntryImpl ace = (AccessControlEntryImpl)var9.next();
            this.setAclOnAce(ace, result);
            acesNew.add(ace);
        }

        this.setAces(result, acesNew);
        return result;
    }

    protected Sid createSid(boolean isPrincipal, String sid) {
        return (Sid)(isPrincipal ? new PrincipalSid(sid) : new GrantedAuthoritySid(sid));
    }

    public final void setPermissionFactory(PermissionFactory permissionFactory) {
        this.permissionFactory = permissionFactory;
    }

    public final void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    public final void setSelectClause(String selectClause) {
        this.selectClause = selectClause;
    }

    public final void setLookupPrimaryKeysWhereClause(String lookupPrimaryKeysWhereClause) {
        this.lookupPrimaryKeysWhereClause = lookupPrimaryKeysWhereClause;
    }

    public final void setLookupObjectIdentitiesWhereClause(String lookupObjectIdentitiesWhereClause) {
        this.lookupObjectIdentitiesWhereClause = lookupObjectIdentitiesWhereClause;
    }

    public final void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public final void setAclClassIdSupported(boolean aclClassIdSupported) {
        if (aclClassIdSupported) {
            Assert.isTrue(this.selectClause.equals("select acl_object_identity.object_id_identity, acl_entry.ace_order,  acl_object_identity.id as acl_id, acl_object_identity.parent_object, acl_object_identity.entries_inheriting, acl_entry.id as ace_id, acl_entry.mask,  acl_entry.granting,  acl_entry.audit_success, acl_entry.audit_failure,  acl_sid.principal as ace_principal, acl_sid.sid as ace_sid,  acli_sid.principal as acl_principal, acli_sid.sid as acl_sid, acl_class.class from acl_object_identity left join acl_sid acli_sid on acli_sid.id = acl_object_identity.owner_sid left join acl_class on acl_class.id = acl_object_identity.object_id_class   left join acl_entry on acl_object_identity.id = acl_entry.acl_object_identity left join acl_sid on acl_entry.sid = acl_sid.id  where ( "), "Cannot set aclClassIdSupported and override the select clause; just override the select clause");
            this.selectClause = "select acl_object_identity.object_id_identity, acl_entry.ace_order,  acl_object_identity.id as acl_id, acl_object_identity.parent_object, acl_object_identity.entries_inheriting, acl_entry.id as ace_id, acl_entry.mask,  acl_entry.granting,  acl_entry.audit_success, acl_entry.audit_failure,  acl_sid.principal as ace_principal, acl_sid.sid as ace_sid,  acli_sid.principal as acl_principal, acli_sid.sid as acl_sid, acl_class.class , acl_class.class_id_type  from acl_object_identity left join acl_sid acli_sid on acli_sid.id = acl_object_identity.owner_sid left join acl_class on acl_class.id = acl_object_identity.object_id_class   left join acl_entry on acl_object_identity.id = acl_entry.acl_object_identity left join acl_sid on acl_entry.sid = acl_sid.id  where ( ";
        }

    }

    private static class StubAclParent implements Acl {
        private final Long id;

        public StubAclParent(Long id) {
            this.id = id;
        }

        public List<AccessControlEntry> getEntries() {
            throw new UnsupportedOperationException("Stub only");
        }

        public Long getId() {
            return this.id;
        }

        public ObjectIdentity getObjectIdentity() {
            throw new UnsupportedOperationException("Stub only");
        }

        public Sid getOwner() {
            throw new UnsupportedOperationException("Stub only");
        }

        public Acl getParentAcl() {
            throw new UnsupportedOperationException("Stub only");
        }

        public boolean isEntriesInheriting() {
            throw new UnsupportedOperationException("Stub only");
        }

        public boolean isGranted(List<Permission> permission, List<Sid> sids, boolean administrativeMode) throws NotFoundException, UnloadedSidException {
            throw new UnsupportedOperationException("Stub only");
        }

        public boolean isSidLoaded(List<Sid> sids) {
            throw new UnsupportedOperationException("Stub only");
        }
    }

    private class ProcessResultSet implements ResultSetExtractor<Set<Long>> {
        private final Map<Serializable, Acl> acls;
        private final List<Sid> sids;

        public ProcessResultSet(Map<Serializable, Acl> acls, List<Sid> sids) {
            Assert.notNull(acls, "ACLs cannot be null");
            this.acls = acls;
            this.sids = sids;
        }

        public Set<Long> extractData(ResultSet rs) throws SQLException {
            HashSet parentIdsToLookup = new HashSet();

            while(true) {
                while(true) {
                    long parentId;
                    do {
                        do {
                            if (!rs.next()) {
                                return parentIdsToLookup;
                            }

                            this.convertCurrentResultIntoObject(this.acls, rs);
                            parentId = rs.getLong("parent_object");
                        } while(parentId == 0L);
                    } while(this.acls.containsKey(new Long(parentId)));

                    MutableAcl cached = MyLookupStrategy.this.aclCache.getFromCache(new Long(parentId));
                    if (cached != null && cached.isSidLoaded(this.sids)) {
                        this.acls.put(cached.getId(), cached);
                    } else {
                        parentIdsToLookup.add(new Long(parentId));
                    }
                }
            }
        }

        private void convertCurrentResultIntoObject(Map<Serializable, Acl> acls, ResultSet rs) throws SQLException {
            Long id = new Long(rs.getLong("acl_id"));
            Acl acl = (Acl)acls.get(id);
            boolean auditSuccess;
            if (acl == null) {
                Serializable identifier = (Serializable)rs.getObject("object_id_identity");
                identifier = identifier.toString();
                ObjectIdentity objectIdentity = new ObjectIdentityImpl(rs.getString("class"), identifier);
                Acl parentAcl = null;
                long parentAclId = rs.getLong("parent_object");
                if (parentAclId != 0L) {
                    parentAcl = new MyLookupStrategy.StubAclParent(parentAclId);
                }

                auditSuccess = rs.getBoolean("entries_inheriting");
                Sid owner = MyLookupStrategy.this.createSid(rs.getBoolean("acl_principal"), rs.getString("acl_sid"));
                acl = new AclImpl(objectIdentity, id, MyLookupStrategy.this.aclAuthorizationStrategy, MyLookupStrategy.this.grantingStrategy, parentAcl, (List)null, auditSuccess, owner);
                acls.put(id, acl);
            }

            if (rs.getString("ace_sid") != null) {
                Long aceId = new Long(rs.getLong("ace_id"));
                Sid recipient = MyLookupStrategy.this.createSid(rs.getBoolean("ace_principal"), rs.getString("ace_sid"));
                int mask = rs.getInt("mask");
                Permission permission = MyLookupStrategy.this.permissionFactory.buildFromMask(mask);
                boolean granting = rs.getBoolean("granting");
                auditSuccess = rs.getBoolean("audit_success");
                boolean auditFailure = rs.getBoolean("audit_failure");
                AccessControlEntryImpl ace = new AccessControlEntryImpl(aceId, (Acl)acl, recipient, permission, granting, auditSuccess, auditFailure);
                List<AccessControlEntryImpl> aces = MyLookupStrategy.this.readAces((AclImpl)acl);
                if (!aces.contains(ace)) {
                    aces.add(ace);
                }
            }

        }
    }
}
