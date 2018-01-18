import hudson.*
import hudson.model.*
import hudson.security.*
import jenkins.*
import jenkins.model.*
import java.util.*
import com.michelin.cio.hudson.plugins.rolestrategy.*
import java.lang.reflect.*

def env = System.getenv()

// Roles
def globalRoleAnonymous = "anonymous"
def globalRoleRead = "read"
def globalRoleAdmin = "admin"
def globalRolePower  = "power"
def ldapGroupNameAdmin = env['LDAP_GROUP_NAME_ADMIN'] ?: "ggnlyehvitrd-cdp2cm_O"
def ldapGroupNamePower = env['LDAP_GROUP_NAME_POWER'] ?: "ggnlyehvitrd-cdp2cm"

def jenkinsInstance = Jenkins.getInstance()

Thread.start {
    // Set new authentication strategy
    RoleBasedAuthorizationStrategy roleBasedAuthenticationStrategy = new RoleBasedAuthorizationStrategy()
    jenkinsInstance.setAuthorizationStrategy(roleBasedAuthenticationStrategy)

    Constructor[] constrs = Role.class.getConstructors();
    for (Constructor<?> c : constrs) {
      c.setAccessible(true);
    }

    // Make the method assignRole accessible
    Method assignRoleMethod = RoleBasedAuthorizationStrategy.class.getDeclaredMethod("assignRole", String.class, Role.class, String.class);
    assignRoleMethod.setAccessible(true);

    // Create admin set of permissions
    Set<Permission> adminPermissions = new HashSet<Permission>();
    adminPermissions.add(Permission.fromId("hudson.model.View.Delete"));
    adminPermissions.add(Permission.fromId("hudson.model.Computer.Connect"));
    adminPermissions.add(Permission.fromId("hudson.model.Run.Delete"));
    adminPermissions.add(Permission.fromId("hudson.model.Hudson.UploadPlugins"));
    adminPermissions.add(Permission.fromId("com.cloudbees.plugins.credentials.CredentialsProvider.ManageDomains"));
    adminPermissions.add(Permission.fromId("hudson.model.Computer.Create"));
    adminPermissions.add(Permission.fromId("hudson.model.View.Configure"));
    adminPermissions.add(Permission.fromId("com.sonyericsson.hudson.plugins.gerrit.trigger.PluginImpl.Retrigger"));
    adminPermissions.add(Permission.fromId("hudson.model.Hudson.ConfigureUpdateCenter"));
    adminPermissions.add(Permission.fromId("hudson.model.Computer.Build"));
    adminPermissions.add(Permission.fromId("hudson.model.Item.Configure"));
    adminPermissions.add(Permission.fromId("hudson.model.Hudson.Administer"));
    adminPermissions.add(Permission.fromId("hudson.model.Item.Cancel"));
    adminPermissions.add(Permission.fromId("hudson.model.Item.Read"));
    adminPermissions.add(Permission.fromId("com.cloudbees.plugins.credentials.CredentialsProvider.View"));
    adminPermissions.add(Permission.fromId("hudson.model.Computer.Delete"));
    adminPermissions.add(Permission.fromId("hudson.model.Item.Build"));
    adminPermissions.add(Permission.fromId("hudson.scm.SCM.Tag"));
    adminPermissions.add(Permission.fromId("hudson.model.Item.Discover"));
    adminPermissions.add(Permission.fromId("hudson.model.Hudson.Read"));
    adminPermissions.add(Permission.fromId("com.cloudbees.plugins.credentials.CredentialsProvider.Update"));
    adminPermissions.add(Permission.fromId("hudson.model.Item.Create"));
    adminPermissions.add(Permission.fromId("hudson.model.Item.Move"));
    adminPermissions.add(Permission.fromId("hudson.model.Item.Workspace"));
    adminPermissions.add(Permission.fromId("com.cloudbees.plugins.credentials.CredentialsProvider.Delete"));
    adminPermissions.add(Permission.fromId("hudson.model.View.Read"));
    adminPermissions.add(Permission.fromId("hudson.model.Hudson.RunScripts"));
    adminPermissions.add(Permission.fromId("hudson.model.View.Create"));
    adminPermissions.add(Permission.fromId("hudson.model.Item.Delete"));
    adminPermissions.add(Permission.fromId("com.sonyericsson.hudson.plugins.gerrit.trigger.PluginImpl.ManualTrigger"));
    adminPermissions.add(Permission.fromId("hudson.model.Computer.Configure"));
    adminPermissions.add(Permission.fromId("com.cloudbees.plugins.credentials.CredentialsProvider.Create"));
    adminPermissions.add(Permission.fromId("hudson.model.Computer.Disconnect"));
    adminPermissions.add(Permission.fromId("hudson.model.Run.Update"));

    // Create the admin Role
    Role adminRole = new Role(globalRoleAdmin, adminPermissions);
    roleBasedAuthenticationStrategy.addRole(RoleBasedAuthorizationStrategy.GLOBAL, adminRole);

    // Assign the role
    roleBasedAuthenticationStrategy.assignRole(RoleBasedAuthorizationStrategy.GLOBAL, adminRole, ldapGroupNameAdmin);
    println "Admin role created...OK"
    
    /// Special access for power users
    Set<Permission> powerPermissions = new HashSet<Permission>();
    powerPermissions.add(Permission.fromId("hudson.model.Hudson.Read"));
    powerPermissions.add(Permission.fromId("hudson.model.View.Read"));
    powerPermissions.add(Permission.fromId("hudson.model.Computer.Create"));
    powerPermissions.add(Permission.fromId("hudson.model.Computer.Connect"));
    powerPermissions.add(Permission.fromId("hudson.model.Item.Discover"));
    powerPermissions.add(Permission.fromId("hudson.model.Item.Build"));
    powerPermissions.add(Permission.fromId("hudson.model.Item.Cancel"));

    Role powerRole = new Role(globalRolePower, powerPermissions);
    roleBasedAuthenticationStrategy.addRole(RoleBasedAuthorizationStrategy.GLOBAL, powerRole);
    // Assign the role
    roleBasedAuthenticationStrategy.assignRole(RoleBasedAuthorizationStrategy.GLOBAL, powerRole, ldapGroupNamePower);
    println "Power user role created...OK"

    /// more access for authenticated users
    Set<Permission> authenticatedPermissions = new HashSet<Permission>();
    authenticatedPermissions.add(Permission.fromId("hudson.model.Hudson.Read"));
    authenticatedPermissions.add(Permission.fromId("hudson.model.View.Read"));
    authenticatedPermissions.add(Permission.fromId("hudson.model.Computer.Create"));
    authenticatedPermissions.add(Permission.fromId("hudson.model.Computer.Connect"));
    authenticatedPermissions.add(Permission.fromId("hudson.model.Item.Discover"));

    Role authenticatedRole = new Role(globalRoleRead, authenticatedPermissions);
    roleBasedAuthenticationStrategy.addRole(RoleBasedAuthorizationStrategy.GLOBAL, authenticatedRole);

    // Assign the role
    roleBasedAuthenticationStrategy.assignRole(RoleBasedAuthorizationStrategy.GLOBAL, authenticatedRole, 'authenticated');
    println "Authenticated role created...OK"
    
    /// Read access for anonymous users
    Set<Permission> anonymousPermissions = new HashSet<Permission>();
    anonymousPermissions.add(Permission.fromId("hudson.model.Hudson.Read"));
    anonymousPermissions.add(Permission.fromId("hudson.model.View.Read"));

    Role anonymousRole = new Role(globalRoleAnonymous, anonymousPermissions);
    roleBasedAuthenticationStrategy.addRole(RoleBasedAuthorizationStrategy.GLOBAL, anonymousRole);

    // Assign the role
    roleBasedAuthenticationStrategy.assignRole(RoleBasedAuthorizationStrategy.GLOBAL, anonymousRole, 'anonymous');
    println "Anonymous role created...OK"

    // Save the state
    println "Saving changes."
    jenkinsInstance.save()
}