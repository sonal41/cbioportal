/*
 * Copyright (c) 2016 The Hyve B.V.
 * This code is licensed under the GNU Affero General Public License (AGPL),
 * version 3, or (at your option) any later version.
 */

/*
 * This file is part of cBioPortal.
 *
 * cBioPortal is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package org.mskcc.cbio.portal.scripts;

import java.io.IOException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mskcc.cbio.portal.dao.DaoCancerStudy;
import org.mskcc.cbio.portal.dao.DaoException;
import org.mskcc.cbio.portal.model.CancerStudy;
import org.mskcc.cbio.portal.util.SpringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * Test the import of Segment data into database.
 * @author pieterlukasse
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/applicationContext-dao.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class TestImportCopyNumberSegmentData {

	@Autowired
	ApplicationContext applicationContext;
	
	//To use in test cases where we expect an exception:
	@Rule
	public ExpectedException exception = ExpectedException.none();
    
	
	@Before 
	public void setUp() throws DaoException
	{
		//set it, to avoid this being set to the runtime (not for testing) application context:
		SpringUtil.setApplicationContext(applicationContext);
	}
	
	/**
     * Test importing of Clinical Data File.
     *
     * @throws DaoException Database Access Error.
     * @throws IOException  IO Error.
     */
	@Test
    public void testImportSegmentDataNewStudy() throws Exception {
		//new dummy study to simulate importing clinical data in empty study:
		CancerStudy cancerStudy = new CancerStudy("testnewseg","testnewseg","testnewseg","brca",true);
        DaoCancerStudy.addCancerStudy(cancerStudy);
        
        //CancerStudy study = DaoCancerStudy.getCancerStudyByStableId("testnew");

        String[] args = {
        		"--data","src/test/resources/segment/data_cna_hg19.seg",
        		"--meta","src/test/resources/segment/meta_cna_hg19_seg.txt",
        		"--loadMode", "bulkLoad"
        		};
        ImportCopyNumberSegmentData runner = new ImportCopyNumberSegmentData(args);
        runner.run(); 
        //TODO : fix test to actually store data and add some checks 
       
	}
}
