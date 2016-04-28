/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pv168.pv168project.app;


import cz.muni.fi.pv168.common.DBUtils;
import cz.muni.fi.pv168.common.IllegalEntityException;
import cz.muni.fi.pv168.common.ServiceFailureException;
import cz.muni.fi.pv168.common.ValidationException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.*;
import static java.time.Month.*;
import java.util.List;
import javax.sql.DataSource;
import org.apache.derby.jdbc.EmbeddedDataSource;
import org.junit.*;
import org.junit.rules.ExpectedException;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;



public class LessonManagerTest {
    
    private LessonManager manager;
    private StudentManager sMng;
    private TeacherManager tMng;
    private DataSource ds;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    //--------------------------------------------------------------------------
    // Test initialization
    //--------------------------------------------------------------------------

    private static DataSource prepareDataSource() throws SQLException {
        EmbeddedDataSource ds = new EmbeddedDataSource();
        // we will use in memory database
        ds.setDatabaseName("memory:Lessonmgr-test");
        // database is created automatically if it does not exist yet
        ds.setCreateDatabase("create");
        return ds;
    }

    @Before
    public void setUp() throws SQLException {
        ds = prepareDataSource();
        DBUtils.executeSqlScript(ds,LessonManager.class.getResource("createTables.sql"));
        manager = new LessonManager();
        manager.setDataSource(ds);
        sMng = new StudentManager();
        sMng.setDataSource(ds);
        tMng = new TeacherManager();
        tMng.setDataSource(ds);
        prepareTestData();
    }

    @After
    public void tearDown() throws SQLException {
        DBUtils.executeSqlScript(ds,LessonManager.class.getResource("dropTables.sql"));
    }
    
    
    //--------------------------------------------------------------------------
    // Preparing test data
    //--------------------------------------------------------------------------

    Student sLowLevel, sHighLevel, sAverage, sLowPrice, sHighPrice, sBadCountry, studentWithNullId, studentNotInDB;
    Teacher tLowLevel, tHighLevel, tLowPrice, tHighPrice, tAverage, tBadCountry, teacherWithNullId, teacherNotInDB;
    Lesson lt1s1, lt1s2, lt2s1, lt2s2, l5;
    
    private void prepareTestData() {

        sLowLevel = new StudentBuilder().fullName("Jozko Mamradmrkvu").skill(2).price(BigDecimal.valueOf(400.00).setScale(2)).region(Region.ENGLAND).build();
        sHighLevel = new StudentBuilder().fullName("Bo Borovsky").skill(8).price(BigDecimal.valueOf(400.00).setScale(2)).region(Region.ENGLAND).build();
        
        sLowPrice = new StudentBuilder().fullName("La Lfsf").skill(4).price(BigDecimal.valueOf(100.00).setScale(2)).region(Region.ENGLAND).build();
        sHighPrice = new StudentBuilder().fullName("Pu Papi").skill(4).price(BigDecimal.valueOf(800.00).setScale(2)).region(Region.ENGLAND).build();
        
        sBadCountry = new StudentBuilder().fullName("Igi Ga").skill(4).price(BigDecimal.valueOf(400.00).setScale(2)).region(Region.INDIA).build();
        
        sAverage = new StudentBuilder().fullName("Petka Plastova").skill(4).price(BigDecimal.valueOf(400.00).setScale(2)).region(Region.ENGLAND).build();
        
        
        tLowLevel = new TeacherBuilder().fullName("Bozkov Bolehlav").skill(2).price(BigDecimal.valueOf(400.00).setScale(2)).region(Region.ENGLAND).build();
        tHighLevel = new TeacherBuilder().fullName("Tequila Blanco").skill(8).price(BigDecimal.valueOf(400.00).setScale(2)).region(Region.ENGLAND).build();
        
        tLowPrice = new TeacherBuilder().fullName("Rasputin Iny").skill(4).price(BigDecimal.valueOf(100.00).setScale(2)).region(Region.ENGLAND).build();
        tHighPrice = new TeacherBuilder().fullName("Rychla Smrt").skill(4).price(BigDecimal.valueOf(800.00).setScale(2)).region(Region.ENGLAND).build();
        
        tAverage = new TeacherBuilder().fullName("Tak Ok").skill(4).price(BigDecimal.valueOf(400.00).setScale(2)).region(Region.ENGLAND).build();
        
        tBadCountry = new TeacherBuilder().fullName("lala Opo").skill(10).price(BigDecimal.valueOf(200.00).setScale(2)).region(Region.INDIA).build();
        
        
        
        sMng.createStudent(sLowLevel);
        sMng.createStudent(sLowPrice);
        sMng.createStudent(sHighLevel);
        sMng.createStudent(sHighPrice);
        sMng.createStudent(sBadCountry);
        sMng.createStudent(sAverage);
        
        tMng.createTeacher(tLowLevel);
        tMng.createTeacher(tLowPrice);
        tMng.createTeacher(tHighLevel);
        tMng.createTeacher(tHighPrice);
        tMng.createTeacher(tAverage);
        tMng.createTeacher(tBadCountry);
        
        /*
        studentWithNullId = new StudentBuilder().id(null).build();
        studentNotInDB = new StudentBuilder().id(sAverage.getId() + 100).build();
        assertThat(sMng.getStudent(studentNotInDB.getId())).isNull();

        teacherWithNullId = new TeacherBuilder().id(null).build();
        teacherNotInDB = new TeacherBuilder().id(tAverage.getId() + 100).build();
        assertThat(tMng.getTeacher(teacherNotInDB.getId())).isNull();
        */
        
        // level <= teacher lvl, price >= teacher price, region = teacher region 
        // level = to student's level (error if teacher<student), price = to teacher's price (error if teacher>student), region = to teacher region (error if teacher != student)
        //lt1s1 = new LessonBuilder().level(s1.getLevel()).price(t1.getPrice()).region(t1.getRegion()).student(s1.getId()).teacher(t1.getId()).build();
        //lt1s2 = new LessonBuilder().level(s2.getLevel()).price(t1.getPrice()).region(t1.getRegion()).student(s2.getId()).teacher(t1.getId()).build();
        //lt2s1 = new LessonBuilder().level(s1.getLevel()).price(t2.getPrice()).region(t2.getRegion()).student(s1.getId()).teacher(t2.getId()).build();
        //lt2s2 = new LessonBuilder().level(s2.getLevel()).price(t2.getPrice()).region(t2.getRegion()).student(s2.getId()).teacher(t2.getId()).build();
        
    }
    //--------------------------------------------------------------------------
    // Testing creating lesson
    //--------------------------------------------------------------------------
  
    @Test
    public void createLesson() {
        Lesson body = new LessonBuilder().price(BigDecimal.valueOf(800.00).setScale(2)).skill(7).region(Region.INDIA).student(sHighPrice.getId()).teacher(tHighPrice.getId()).build();
        manager.createLesson(body);

        Long bodyId = body.getId();
        assertThat(bodyId).isNotNull();

        assertThat(manager.getLesson(bodyId))
                .isNotSameAs(body)
                .isEqualToComparingFieldByField(body);
    }
    
    @Test
    public void createLessonWithNegativeSkill () {
        Lesson lesson = new LessonBuilder().price(BigDecimal.valueOf(800.00).setScale(2)).skill(-1).region(Region.INDIA).student(sAverage.getId()).teacher(tAverage.getId()).build();
        assertThatThrownBy(() -> manager.createLesson(lesson))
                .isInstanceOf(ValidationException.class);
    }
    
    @Test
    public void createLessonWithNullRegion () {
        Lesson lesson = new LessonBuilder().price(BigDecimal.valueOf(800.00).setScale(2)).skill(5).region(null).student(sAverage.getId()).teacher(tAverage.getId()).build();
        assertThatThrownBy(() -> manager.createLesson(lesson))
                .isInstanceOf(ValidationException.class);
    }
    
    @Test
    public void createLessonWithNegativePrice () {
        Lesson lesson = new LessonBuilder().price(BigDecimal.valueOf(800.00).negate().setScale(2)).skill(5).region(null).student(sHighLevel.getId()).teacher(tHighLevel.getId()).build();
        expectedException.expect(ValidationException.class);
        manager.createLesson(lesson);
    }
    
    //--------------------------------------------------------------------------
    // Testing making and finding a match
    //--------------------------------------------------------------------------
    
    
    @Test
    public void makeMatchWithHighSkillStudent () {
        assertThatThrownBy(() -> manager.makeMatch(tAverage, sHighLevel))
                .isInstanceOf(ValidationException.class);
    }
    
    @Test
    public void makeMatchWithLowPriceStudent () {
        assertThatThrownBy(() -> manager.makeMatch(tAverage, sLowPrice))
                .isInstanceOf(ValidationException.class);
    }
    
    @Test
    public void makeMatchWithBadCountryStudent () {
        assertThatThrownBy(() -> manager.makeMatch(tAverage, sBadCountry))
                .isInstanceOf(ValidationException.class);
    }
    
    @Test
    public void makeMatchValid () {
        manager.makeMatch(tAverage, sAverage);
        assertThat(manager.getLesson(tAverage, sAverage)).isNotNull();
    }
    
    @Test
    public void matchForTeacherValid () {
        
        List <Student> list = manager.findMatchForTeacher(tBadCountry);
        assertThat(list.get(0)).isEqualToComparingFieldByField(sBadCountry);
    }
    
    @Test
    public void matchForStudentValid () {
        
        List <Teacher> list = manager.findMatchForStudent(sBadCountry);
        assertThat(list.get(0)).isEqualToComparingFieldByField(tBadCountry);
    }
    
    @Test
    public void matchForStudentIsNull () {
        assertThatThrownBy(() -> manager.findMatchForStudent(null))
                .isInstanceOf(IllegalArgumentException.class);
    }
    
    //--------------------------------------------------------------------------
    // Testing updating lesson
    //--------------------------------------------------------------------------
    
    @FunctionalInterface
    private static interface Operation<T> {
        void callOn(T subjectOfOperation);
    }
    
    private void testUpdateLesson(Operation<Lesson> updateOperation) {
        Lesson l1 = new LessonBuilder().price(BigDecimal.valueOf(800.00).setScale(2)).skill(5).region(Region.NORTH_AMERICA).student(sAverage.getId()).teacher(tAverage.getId()).build();
        Lesson l2 = new LessonBuilder().price(BigDecimal.valueOf(300.00).setScale(2)).skill(3).region(Region.RUSSIAN).student(sLowLevel.getId()).teacher(tLowLevel.getId()).build();
        manager.createLesson(l1);
        manager.createLesson(l2);

        updateOperation.callOn(l1);

        manager.updateLesson(l1);
        
        assertThat(manager.getLesson(l1.getId()))
                .isEqualToComparingFieldByField(l1);
        // Check if updates didn't affected other records
        assertThat(manager.getLesson(l2.getId()))
                .isEqualToComparingFieldByField(l2);
    }
    
    @Test
    public void updateLessonRegion() {
        testUpdateLesson((grave) -> grave.setRegion(Region.INDIA));
    }

    @Test
    public void updateLessonPrice() {
        testUpdateLesson((grave) -> grave.setPrice(BigDecimal.valueOf(1000.10).setScale(2)));
    }
    
    //--------------------------------------------------------------------------
    // Tests if GraveManager methods throws ServiceFailureException in case of
    // DB operation failure
    //--------------------------------------------------------------------------

    private void testExpectedServiceFailureException(Operation<LessonManager> operation) throws SQLException {
        SQLException sqlException = new SQLException();
        DataSource failingDataSource = mock(DataSource.class);
        when(failingDataSource.getConnection()).thenThrow(sqlException);
        manager.setDataSource(failingDataSource);
        assertThatThrownBy(() -> operation.callOn(manager))
                .isInstanceOf(ServiceFailureException.class)
                .hasCause(sqlException);
    }

    @Test
    public void findAllLessonsWithSqlExceptionThrown() throws SQLException {
        testExpectedServiceFailureException((lessonManager) -> lessonManager.findAllLessons());
    }

    @Test
    public void getLessonIdWithSqlExceptionThrown() throws SQLException {
        testExpectedServiceFailureException((cemeteryManager) -> cemeteryManager.getLesson(1L));
    }

    @Test
    public void getLessonStudentWithSqlExceptionThrown() throws SQLException {
        testExpectedServiceFailureException((cemeteryManager) -> cemeteryManager.getLesson(sAverage));
    }

    @Test
    public void deleteLessonWithSqlExceptionThrown() throws SQLException {
        Lesson lesson = new LessonBuilder().skill(sAverage.getSkill()).price(tAverage.getPrice()).region(tAverage.getRegion()).student(sAverage.getId()).teacher(tAverage.getId()).build();
        lesson.setId(1L);
        testExpectedServiceFailureException((cemeteryManager) -> cemeteryManager.deleteLesson(lesson));
    }

    @Test
    public void makeMatchWithSqlExceptionThrown() throws SQLException {
        testExpectedServiceFailureException((cemeteryManager) -> cemeteryManager.makeMatch(tAverage, sAverage));
    }

    @Test
    public void createLessonWithSqlExceptionThrown() throws SQLException {
        Lesson lesson = new LessonBuilder().skill(sAverage.getSkill()).price(tAverage.getPrice()).region(tAverage.getRegion()).student(sAverage.getId()).teacher(tAverage.getId()).build();
        testExpectedServiceFailureException((cemeteryManager) -> cemeteryManager.createLesson(lesson));
    }

}
