package asia.ncc.estimation.tool.repository;


import asia.ncc.estimation.tool.domain.Assumption;
import asia.ncc.estimation.tool.service.dto.CountDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
    @Transactional
    public class TestRepository {
        @Autowired
        JdbcTemplate jdbcTemplate;
        public int deleteWBSByIdWORK_ITEMandIdPROJECT(long idW,long idP) {
            return jdbcTemplate.update("delete from WBS where WORK_ITEM_IDS_ID=? and PROJECTS_ID=?", new Object[] { idW,idP });
        }
        public int deleteWBSByIdWORK_ITEM(long id) {
            return jdbcTemplate.update("delete from WBS where WORK_ITEM_IDS_ID=?", new Object[] { id });
        }
        public int deleteWBSByIdPROJECTS(long id) {
            return jdbcTemplate.update("delete from WBS where PROJECTS_ID=?", new Object[] { id });
        }
        public int deleteAssumptiopnByIdWorkItem(long id) {
            return jdbcTemplate.update("delete from ASSUMPTION where WORK_ITEM_ID=?", new Object[] { id });
        }
        public long findIdquotationByPidCid(Long idP,Long idC){
            System.out.println("Id project: "+idP+"/nId Customer: "+idC);
            String sql = "SELECT id from quotation where projectid_id= " +idP +" and customer_id= "+ idC;
            Long id=null;
            try {
            id = this.jdbcTemplate.queryForObject(sql, Long.class);
            } catch (EmptyResultDataAccessException e) {
                return 0;
            }
            System.out.println("Id quotation: "+id);
            return (id != null ? id : 0);
        }
        public int deleteCustomerByIdQuotation(long id){
            return jdbcTemplate.update("delete from CUSTOMER where QUOTATION_ID=?", new Object[] { id });
        }
        public List<Assumption> findAssByIdWork(long workitemId){
            String sql = "SELECT * FROM assumption WHERE work_item_id=" + workitemId;
            List<Assumption> assumptionList = this.jdbcTemplate.query(sql, new RowMapper<Assumption>() {
                public Assumption mapRow(ResultSet rs, int rowNum) throws SQLException {
                    Assumption assumption = new Assumption();
                    assumption.setId(rs.getLong("id"));
                    assumption.setNote(rs.getString("note"));
                    assumption.setContent(rs.getString("content"));
                    return assumption;
                }
            });
            return assumptionList;
        }
        public CountDTO findcount(){
            CountDTO countDTO = new CountDTO();
            String sql1 = "select count(*) from project";
            String sql2 = "select count(*) from quotation";
            String sql3 = "select count(*) from customer";
            String sql4 = "select count(*) from work_item";
            int project = this.jdbcTemplate.queryForObject(sql1,Integer.class);
            int quotation = this.jdbcTemplate.queryForObject(sql2,Integer.class);
            int customer = this.jdbcTemplate.queryForObject(sql3,Integer.class);
            int work_item = this.jdbcTemplate.queryForObject(sql4,Integer.class);

            System.out.println(project);
            System.out.println(quotation);
            System.out.println(customer);
            System.out.println(work_item);

            countDTO.setProject(project);
            countDTO.setQuotation(quotation);
            countDTO.setCustomer(customer);
            countDTO.setWorkitem(work_item);
            return countDTO;
        }

    }
