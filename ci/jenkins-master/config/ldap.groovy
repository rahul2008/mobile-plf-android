import jenkins.model.*
import hudson.security.*
import org.jenkinsci.plugins.*

def instance = Jenkins.getInstance()

def ldapUser = new File("/run/secrets/ldap-user").text.trim()
def ldapPass = new File("/run/secrets/ldap-pass").text.trim()

String server = 'nlvhtcway1dc001.code1.emi.philips.com'
String rootDN = 'DC=code1,DC=emi,DC=philips,DC=com'
String userSearchBase = ''
String userSearch = 'cn={0}'
String groupSearchBase = 'OU=NLVEHVRES2,OU=CODE'
String bindDn = 'CN='+ldapUser+',OU=Users,OU=NLVEHVRES2,OU=CODE,DC=code1,DC=emi,DC=philips,DC=com'
String bindPassword = ldapPass
boolean inhibitInferRootDN = false
    
SecurityRealm ldap_realm = new LDAPSecurityRealm(server, rootDN, userSearchBase, userSearch, groupSearchBase, bindDn, bindPassword, inhibitInferRootDN) 
instance.setSecurityRealm(ldap_realm)
instance.save()

//If unsecured use this to prevent lockout
def authStrategy = Hudson.instance.getAuthorizationStrategy()
if (authStrategy instanceof AuthorizationStrategy.Unsecured) {
  println "Defaulting to 'Authenticated users can do anything' rather than 'unsecure'."
  instance.setAuthorizationStrategy(new FullControlOnceLoggedInAuthorizationStrategy())
}