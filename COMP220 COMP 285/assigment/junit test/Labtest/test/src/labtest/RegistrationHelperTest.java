/**
 * 
 */
package labtest;

import static org.junit.Assert.*;

import java.util.Vector;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Bran.Kim
 *
 */
public class RegistrationHelperTest {
	RegistrationHelper registrationhelper= new RegistrationHelper();

	String u1,p1,u2,p2,u3,p3,u4,p4,u5,p5,u6,p6,u7,p7,u8,p8,u9,p9,
	u10,p10,u11,p11,u12,p12,u13,p13,u14,p14,u15,p15,u16,p16,u17,p17,u18,p18,u19,p19,
	u20,p20,u21,p21,u22,p22,u23,p23,u24,p24,u25,p25,u26,p26,u27,p27,u28,p28,u29,p29,
	u30,p30,u31,p31,u32,p32,u33,p33,u34,p34,u35,p35,u36,p36,u37,p37,u38,p38,u39,p39,
	u40,p40,u41,p41,u42,p42,u43,p43;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		u1=null;
		p1="Liverpool2020$";
		u2="comp220285";
		p2=null;
		u3="comp22028";
		p3="Liverpool2020$ab";
		u4="c";
		p4="Liverpool2020$";
		u5="co";
		p5="Liverpool2020$";
		u6="com";
		p6="Liverpool2020$";		
		u7="comp2";
		p7="Liverpool2020$";
		u8="comp22";
		p8="Liverpool2020$";
		u9="comp220";
		p9="Liverpool2020$";
		u10="comp2202";
		p10="Liverpool2020$abc";
		u11="comp22028";
		p11="Liverpool2020$abc";
		u12="comp220285";
		p12="Liverpool2020$abc";
		u13="comp220285a";
		p13="Liverpool2020$abc";
		u14="comp220285ab";
		p14="Liverpool2020$abc";
		u15="comp220285abc";
		p15="Liverpool2020$abc";
		u16="1ompabcdef";
		p16="Liverpool2020$abc";
		u17="compabcdef";
		p17="Liverpool2020$abc";
		u18="c234567891";
		p18="Liverpool2020$abc";
		
		
		u19="comp220285";
		p19="$";
		u20="comp220285";
		p20="L$";
		u21="comp220285";
		p21="Li$";		
		u22="comp220285";
		p22="Liverpool$";
		u23="comp220285";
		p23="Liverpool2$";
		u24="comp220285";
		p24="Liverpool20$";
		u25="comp220285";
		p25="Liverpool202$";
		u26="comp220285";
		p26="Liverpool2020$";		
		u27="comp220285";
		p27="Liverpool2020$1";
		u28="comp220285";
		p28="Liverpool2020$12";
		u29="comp220285";
		p29="LIVERPOOL2020$";
		u30="comp220285";
		p30="LIVERPOOL2020$a";
		u31="comp220285";
		p31="lIVERPOOL2020$A";		
		u32="comp220285";
		p32="liverpool2020$";
		u33="comp220285";
		p33="liverpool2020$A";
		u34="comp220285";
		p34="Liverpool2020$a";
		u35="comp220285";
		p35="Liverpoolabcd$";
		u36="comp220285";
		p36="1Liverpoolabcd$";		
		u37="comp220285";
		p37="Liverpoolabcd$1";
		u38="comp220285";
		p38="Liverpool2020$";
		u39="comp220285";
		p39="Liverpool2020*";
		u40="comp220285";
		p40="Liverpool2020%";
		u41="comp220285";
		p41="$Liverpool2020";		
		u42="comp220285";
		p42="Liverpool2020?";
		u43="comp220285"; 
		p43="Liverpool2020";
		
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		
	}

	/**
	 * Test method for {@link labtest.RegistrationHelper#checkUsernamePassword(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testCheckUsernamePassword() {
	
	/**
	 * Check username is not null 
	 * u1=null;                    
	   p1="Liverpool2020$";               
	 */
	boolean test1=	registrationhelper.checkUsernamePassword(u1,p1);
	assertFalse("WRONG! null Username shouldn't work. ",test1);
	
	/**
	 * Check password is not null
	 * u2="comp220285"; p2=null; 
	 * u3="comp22028"; p3="Liverpool2020$abc";
	 */
	boolean test2=	registrationhelper.checkUsernamePassword(u2,p2);
	assertFalse("WRONG! null Password shouldn't work. ",test2);
	boolean test3=	registrationhelper.checkUsernamePassword(u3,p3);
	assertTrue("WRONG! non-null Username or Password should work. ",test3);	
	
	/**
	 * Check username is long enough
	 * u4="c"; p4="Liverpool2020$";
	   u5="co"; p5="Liverpool2020$";
	   u6="com"; p6="Liverpool2020$";		
	   u7="comp2"; p7="Liverpool2020$";
	   u8="comp22"; p8="Liverpool2020$";
	   u9="comp220"; p9="Liverpool2020$";
	   u10="comp2202"; p10="Liverpool2020$abc";
	   u11="comp22028"; p11="Liverpool2020$abc";
	 */
	boolean test4=	registrationhelper.checkUsernamePassword(u4,p4);
	assertFalse("WRONG! Username length is 1 shouldn't work. ",test4);
	boolean test5=	registrationhelper.checkUsernamePassword(u5,p5);
	assertFalse("WRONG! Username length is 2 shouldn't work. ",test5);
	boolean test6=	registrationhelper.checkUsernamePassword(u6,p6);
	assertFalse("WRONG! Username length is 3 shouldn't work. ",test6);
	boolean test7=	registrationhelper.checkUsernamePassword(u7,p7);
	assertFalse("WRONG! Username length is 5 shouldn't work. ",test7);
	boolean test8=	registrationhelper.checkUsernamePassword(u8,p8);
	assertFalse("WRONG! Username length is 6 shouldn't work. ",test8);
	boolean test9=	registrationhelper.checkUsernamePassword(u9,p9);
	assertFalse("WRONG! Username length is 7 shouldn't work. ",test9);
	boolean test10=	registrationhelper.checkUsernamePassword(u10,p10);
	assertTrue("WRONG! Username length is 8 should work. ",test10);
	boolean test11=	registrationhelper.checkUsernamePassword(u11,p11);
	assertTrue("WRONG! Username length is 9 should work. ",test11);
	
	/**
	 * Check username is not too long
	 *  u12="comp220285"; p12="Liverpool2020$abc";
		u13="comp220285a"; p13="Liverpool2020$abc";
		u14="comp220285ab"; p14="Liverpool2020$abc";
		u15="comp220285abc"; p15="Liverpool2020$abc";
		
	 */
	boolean test12=	registrationhelper.checkUsernamePassword(u12,p12);
	assertTrue("WRONG! Username length is 10 should work. ",test12);
	boolean test13=	registrationhelper.checkUsernamePassword(u13,p13);
	assertFalse("WRONG! Username length is 11 shouldn't work. ",test13);
	boolean test14=	registrationhelper.checkUsernamePassword(u14,p14);
	assertFalse("WRONG! Username length is 12 shouldn't work. ",test14);
	boolean test15=	registrationhelper.checkUsernamePassword(u15,p15);
	assertFalse("WRONG! Username length is 13 shouldn't work. ",test15);
	
	/**
	 * Check username starts with an alphabetic character
	 *  u16="1ompabcdef"; p16="Liverpool2020$abc";
		u17="compabcdef"; p17="Liverpool2020$abc";
		u18="c234567891"; p18="Liverpool2020$abc";
	 */
	boolean test16=	registrationhelper.checkUsernamePassword(u16,p16);
	assertFalse("WRONG! Username start with number shouldn't work. ",test16);
	boolean test17=	registrationhelper.checkUsernamePassword(u17,p17);
	assertTrue("WRONG! Username start with alphabetic should work. ",test17);
	boolean test18=	registrationhelper.checkUsernamePassword(u18,p18);
	assertTrue("WRONG! Username start with alphabetic should work. ",test18);
	
	/**
	 * Check password is long enough
	 *  u19="comp220285"; p19="$";
		u20="comp220285"; p20="L$";
		u21="comp220285"; p21="Li$";		
		u22="comp220285"; p22="Liverpool$";
		u23="comp220285"; p23="Liverpool2$";
		u24="comp220285"; p24="Liverpool20$";
		u25="comp220285"; p25="Liverpool202$";
		u26="comp220285"; p26="Liverpool2020$";		
		u27="comp220285"; p27="Liverpool2020$1";
		u28="comp220285"; p28="Liverpool2020$12";
	 */
	boolean test19=	registrationhelper.checkUsernamePassword(u19,p19);
	assertFalse("WRONG! Password length is 1 shouldn't work. ",test19);
	boolean test20=	registrationhelper.checkUsernamePassword(u20,p20);
	assertFalse("WRONG! Password length is 2 shouldn't work. ",test20);
	boolean test21=	registrationhelper.checkUsernamePassword(u21,p21);
	assertFalse("WRONG! Password length is 3 shouldn't work. ",test21);
	boolean test22=	registrationhelper.checkUsernamePassword(u22,p22);
	assertFalse("WRONG! Password length is 10 shouldn't work. ",test22);
	boolean test23=	registrationhelper.checkUsernamePassword(u23,p23);
	assertFalse("WRONG! Password length is 11 shouldn't work. ",test23);
	boolean test24=	registrationhelper.checkUsernamePassword(u24,p24);
	assertFalse("WRONG! Password length is 12 shouldn't work. ",test24);
	boolean test25=	registrationhelper.checkUsernamePassword(u25,p25);
	assertTrue("WRONG! Password length is 13 should work. ",test25);
	boolean test26=	registrationhelper.checkUsernamePassword(u26,p26);
	assertTrue("WRONG! Password length is 14 should work. ",test26);
	boolean test27=	registrationhelper.checkUsernamePassword(u27,p27);
	assertTrue("WRONG! Password length is 15 should work. ",test27);
	boolean test28=	registrationhelper.checkUsernamePassword(u28,p28);
	assertTrue("WRONG! Password length is 16 should work. ",test28);
	
	/**
	 * Now look for a lower case char in the password, no lower case then check fails
	 *  u29="comp220285"; p29="LIVERPOOL2020$";
		u30="comp220285"; p30="LIVERPOOL2020$a";
		u31="comp220285"; p31="lIVERPOOL2020$A";
	 */
	boolean test29=	registrationhelper.checkUsernamePassword(u29,p29);
	assertFalse("WRONG! Password no lower case shouldn't work. ",test29);
	boolean test30=registrationhelper.checkUsernamePassword(u30,p30);
	assertTrue("WRONG! Password has lower case should work. ",test30);
	boolean test31=	registrationhelper.checkUsernamePassword(u31,p31);
	assertTrue("WRONG! Password has lower case should work. ",test31);
	
	/**
	 * Now look for an upper case char in the password, no upper case then check fails
	 *  u32="comp220285"; p32="liverpool2020$";
		u33="comp220285"; p33="liverpool2020$A";
		u34="comp220285"; p34="Liverpool2020$a";
	 */
	boolean test32=	registrationhelper.checkUsernamePassword(u32,p32);
	assertFalse("WRONG! Password no upper case shouldn't work. ",test32);
	boolean test33=	registrationhelper.checkUsernamePassword(u33,p33);
	assertTrue("WRONG! Password has upper case should work. ",test33);
	boolean test34=	registrationhelper.checkUsernamePassword(u34,p34);
	assertTrue("WRONG! Password has upper case should work. ",test34);
	
	/**
	 * Now look for an digit in the password, no digit then the test fails
	 *  u35="comp220285"; p35="Liverpoolabcd$";
		u36="comp220285"; p36="1Liverpoolabcd$";		
		u37="comp220285"; p37="Liverpoolabcd$1";	
	 */
	boolean test35=	registrationhelper.checkUsernamePassword(u35,p35);
	assertFalse("WRONG! Password no digit shouldn't work. ",test35);
	boolean test36=	registrationhelper.checkUsernamePassword(u36,p36);
	assertTrue("WRONG! Password has digit should work. ",test36);
	boolean test37=	registrationhelper.checkUsernamePassword(u37,p37);
	assertTrue("WRONG! Password has digit should work. ",test37);
	
	/*
	 * Now look for a special char in the password, no special char then test fails
	 *  u38="comp220285"; p38="Liverpool2020$";
		u39="comp220285"; p39="Liverpool2020*";
		u40="comp220285"; p40="Liverpool2020%";
		u41="comp220285"; p41="$Liverpool2020";		
		u42="comp220285"; p42="Liverpool2020?";
		u43="comp220285"; p43="Liverpool2020";
	 */
	boolean test38=	registrationhelper.checkUsernamePassword(u38,p38);
	assertTrue("WRONG! Password has special char '$' should work. ",test38);
	boolean test39=	registrationhelper.checkUsernamePassword(u39,p39);
	assertTrue("WRONG! Password has special char '*' should work. ",test39);
	boolean test40=	registrationhelper.checkUsernamePassword(u40,p40);
	assertTrue("WRONG! Password has special char '%' should work. ",test40);
	boolean test41=	registrationhelper.checkUsernamePassword(u41,p41);
	assertTrue("WRONG! Password has special char should work. ",test41);
	boolean test42=	registrationhelper.checkUsernamePassword(u42,p42);
	assertFalse("WRONG! Password has other special char shouldn't work. ",test42);
	boolean test43=	registrationhelper.checkUsernamePassword(u43,p43);
	assertFalse("WRONG! Password no special char shouldn't work. ",test43);
	}

}
