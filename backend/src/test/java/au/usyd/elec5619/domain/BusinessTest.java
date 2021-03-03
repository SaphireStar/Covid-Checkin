package au.usyd.elec5619.domain;

import junit.framework.TestCase;

public class BusinessTest extends TestCase {

    private Business b;

    protected void setUp() throws Exception {
        b = new Business();
    }

    public void testSetAndGetBusinessName() {
        String testName = "bName";
        assertNull(b.getBusinessName());
        b.setBusinessName(testName);
        assertEquals(testName, b.getBusinessName());
    }
    
    public void testSetAndGetBusinessEmail() {
        String testEmail = "@email";
        assertNull(b.getBusinessEmail());
        b.setBusinessEmail(testEmail);
        assertEquals(testEmail, b.getBusinessEmail());
    }
    
    public void testSetAndGetBusinessPhoto() {
        String test = "photo.png";
        assertNull(b.getPhoto());
        b.setPhoto(test);
        assertEquals(test, b.getPhoto());
    }

    public void testSetAndGetBusinessAddress() {
        String test = "123 street";
        assertNull(b.getAddress());
        b.setAddress(test);
        assertEquals(test, b.getAddress());
    }

    public void testSetAndGetBusinessPostcode() {
        int testPostCode = 1234;
        assertEquals(0, b.getPostcode());
        b.setPostcode(testPostCode);
        assertEquals(testPostCode, b.getPostcode());
    }
    
    public void testSetAndGetBusinessCapacity() {
        int testCapacity = 12;
        assertEquals(0, b.getCapacity());
        b.setCapacity(testCapacity);
        assertEquals(testCapacity, b.getCapacity());
    }
    
    public void testSetAndGetPhoneNumber() {
        String testNumb = "123456789";
        assertNull(b.getPhoneNumber());
        b.setPhoneNumber(testNumb);
        assertEquals(testNumb, b.getPhoneNumber());
    }
    
    public void testSetAndGetBusinessRepId() {
        int businessRepId = 12312;
        assertEquals(0, b.getBusinessRepId());
        b.setBusinessRepId(businessRepId);
        assertEquals(businessRepId, b.getBusinessRepId());
    }

  
}
