package JunitTest;

import static org.junit.Assert.*;

import org.junit.Test;

public class JunitTestTest {

	JunitTest t = new JunitTest();
    @Test
    public void testIsTriangle() {
    	//the grahic is not a triangle
    	assertEquals(-1, t.isTriangle(1, 2, 3));
    	
    	assertEquals(-1, t.isTriangle(3, 1, 2));
    	
    	assertEquals(-1, t.isTriangle(2, 3, 1));
    	
    	assertEquals(-1, t.isTriangle(-1, 1, 1));
    	
    	assertEquals(-1, t.isTriangle(1, -1, 1));
    	
    	assertEquals(-1, t.isTriangle(1, 1, -1));
    	
    	//equilateral
    	assertEquals(1, t.isTriangle(1, 1, 1));
    	
    	//isosceles
    	assertEquals(2, t.isTriangle(2, 2, 3));
    	
    	assertEquals(2, t.isTriangle(2, 3, 3));
    	
    	assertEquals(2, t.isTriangle(2, 3, 2));
    	
    	//scalene
    	
    	assertEquals(3, t.isTriangle(2, 3, 4));
    }

}
