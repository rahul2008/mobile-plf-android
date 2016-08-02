window.onload = LoadPage;

var sd;
var lfc;
var store;
var tf;
var mf;
var lc;
var lang = 'CSharp';

function ListController() {
    this.tabCollections = new Array();
    this.listCollections = new Array();
    this.tabCollectionIds = new Array();
    this.listCollectionIds = new Array();
}

ListController.prototype.registerTabbedArea = function(tabCollection, listCollection, tabCollectionId, listCollectionId, filter) {
    this.tabCollections.push(tabCollection);
    this.listCollections.push(listCollection);
    this.tabCollectionIds.push(tabCollectionId);
    this.listCollectionIds.push(listCollectionId);
}

function LoadPage() {
    store = new CookieDataStore('docs');
    registerEventHandler(window, 'load', 
        function() { var ss = new SplitScreen('control','main'); selectLanguage(store.get('lang')); });
    sd = getStyleDictionary(); 
    lfc = new LanguageFilterController();
    lc = new ListController();
    tf = new TypeFilter();
    mf = new MemberFilter();
    
    setUpLanguage();
    
    setUpSyntax();
   
	setUpSnippets();
	
	setUpType();
	
	setUpAllMembers();
}

function setUpLanguage() {
    var langFilter = document.getElementById('languageFilter');
    if (langFilter == null) return;
  
    var options = langFilter.getElementsByTagName('option');   
    if (options == null) return;
    
    var value = options[0].getAttribute('value');
    var names = value.split(' ');
    lang = names[0];
}

function setUpSnippets() {
    var divs = document.getElementsByTagName("DIV");
   
    for (var i = 0; i < divs.length; i++) {
        var temp = i;
        var name =  divs[i].getAttribute("name");
        if (name == null || name != "snippetGroup") continue;
        processSection(divs[i], 'x-lang', lang, true, true, lfc);
        i= temp + 1;
    }
}

function setUpSyntax() {
    var syntax = document.getElementById('syntaxSection');
    if (syntax == null) return;
    
    processSection(syntax, 'x-lang', lang, true, true, lfc);
    
    var usyntax = document.getElementById('usyntaxSection');
    if (usyntax == null) return;
    
    processSection(usyntax, 'x-lang', lang, true, true, lfc);
}

function setUpType() {
    var typeSection = document.getElementById('typeSection');
    if (typeSection == null) return;
    
    processSection(typeSection, 'value', 'all', true, false, lc);
}

function setUpAllMembers() {
    var allMembersSection = document.getElementById('allMembersSection');
    if (allMembersSection == null) return;
    
    processSection(allMembersSection, 'value', 'all', true, false, lc);
}

function processSection(section, attribute, value, toggleClassValue, toggleStyleValue, registerObject) {
    var nodes = section.childNodes;
        
    var tabs;
    var blocks;
    var tabId;
    var blockId;
       
    if(nodes.length != 2) return;
    
    if(nodes[0].tagName == 'TABLE') {
        var rows = nodes[0].getElementsByTagName('tr');
           
        if (rows.length == 0) return;
        
        tabId = rows[0].getAttribute('id');
           
        if (tabId == null) return;
           
        tabs = new ElementCollection(tabId);
    }
  
    if(nodes[1].tagName == 'DIV') {
        blockId = nodes[1].getAttribute('id');
        if (blockId == null) return;
            
        blocks = new ElementCollection(blockId);
    }
    else if (nodes[1].tagName == 'TABLE') {
        blockId = nodes[1].getAttribute('id');
        if (blockId == null) return;
            
        blocks = new ElementCollection(blockId);
    }
       
    if (registerObject != null) registerObject.registerTabbedArea(tabs, blocks, tabId, blockId);
    if (toggleClassValue) tabs.toggleClass(attribute,value,'activeTab','tab');
	if (toggleStyleValue) blocks.toggleStyle(attribute,value,'display','block','none');
}

function toggleClass(id, attributeName, attributeValue, trueClass, falseClass) {
    for(var i = 0; i < lfc.tabCollections.length; i++) {
        var tabs = lfc.tabCollections[i];
              
        if (lfc.tabCollectionIds[i] == id) { 
            tabs.toggleClass(attributeName, attributeValue, trueClass, falseClass);
        }
    }  
      
    for(var j = 0; j < lc.tabCollections.length; j++) {
        var listTabs = lc.tabCollections[j];
        
        if (lc.tabCollectionIds[j] == id) {
            listTabs.toggleClass(attributeName, attributeValue, trueClass, falseClass);
        }
    }
}

function toggleStyle(id, attributeName, attributeValue, styleName, trueStyleValue, falseStyleValue) {
    for (var i = 0; i < lfc.blockCollections.length; i++) {
       
        var blocks = lfc.blockCollections[i];
              
        if (lfc.blockCollectionIds[i] == id) {
            blocks.toggleStyle(attributeName, attributeValue, styleName, trueStyleValue, falseStyleValue);
        }
    }
}

function processList(id, methodName, typeName) {
    for (var i = 0; i < lc.listCollections.length; i++) {
        var list = lc.listCollections[i];
        if (lc.listCollectionIds[i] == id) {
            if (typeName == 'type') list.process(getInstanceDelegate(tf,methodName));
            else if (typeName == 'member') list.process(getInstanceDelegate(mf, methodName));
        }
    }
}

function processSubgroup(subgroup, typeName) {
    if (typeName == 'type' && tf != null) tf.subgroup = subgroup;
    else if (typeName == 'member' && mf != null) mf.subgroup = subgroup;
}

function toggleCheckState(visibility, value) {
    if (mf == null) return;
    mf[visibility] = value;
}

function switchLanguage(names, value) {
    if (lfc != null) lfc.switchLanguage(names[0]); 
    store.set('lang',value); 
    store.save();
}

/*
**********
**********   Begin Copy Code
**********
*/

function CopyCode(key)
{
	var trElements = document.getElementsByTagName("tr");
	var i;
	for(i = 0; i < trElements.length; ++i)
	{
		if(key.parentNode.parentNode.parentNode == trElements[i].parentNode)
		{
		    if (window.clipboardData)
            {
                // the IE-manner
                window.clipboardData.setData("Text", trElements[i].innerText);
            }
            else if (window.netscape)
            {
                // Gives unrestricted access to browser APIs using XPConnect
		try
		{
			netscape.security.PrivilegeManager.enablePrivilege("UniversalXPConnect");
		}
	        catch(e)
		{
			alert("Universal Connect was refused, cannot copy to " +
				"clipboard.  Go to about:config and set " +
				"signed.applets.codebase_principal_support to true to " +
				"enable clipboard support.");
			return;
		}

                // Creates an instance of nsIClipboard
                var clip = Components.classes['@mozilla.org/widget/clipboard;1'].createInstance(Components.interfaces.nsIClipboard);
                if (!clip) return;

                // Creates an instance of nsITransferable
                var trans = Components.classes['@mozilla.org/widget/transferable;1'].createInstance(Components.interfaces.nsITransferable);
                if (!trans) return;

                // register the data flavor
                trans.addDataFlavor('text/unicode');

                // Create object to hold the data
                var str = new Object();

                // Creates an instance of nsISupportsString
                var str = Components.classes["@mozilla.org/supports-string;1"].createInstance(Components.interfaces.nsISupportsString);

                //Assigns the data to be copied
                var copytext = trElements[i].textContent;
                str.data = copytext;

                // Add data objects to transferable
                trans.setTransferData("text/unicode",str,copytext.length*2);
                var clipid = Components.interfaces.nsIClipboard;
                if (!clip) return false;

                // Transfer the data to clipboard
                clip.setData(trans,null,clipid.kGlobalClipboard);
            }
        }
	}
}

function ChangeCopyCodeIcon(key)
{
	var i;
	var imageElements = document.getElementsByName("ccImage")
	for(i=0; i<imageElements.length; ++i)
	{
		if(imageElements[i].parentNode == key)
		{
			if(imageElements[i].src == copyImage.src)
			{
				imageElements[i].src = copyHoverImage.src;
				imageElements[i].alt = copyHoverImage.alt;
				key.className = 'copyCodeOnHover';
			}
			else
			{
				imageElements[i].src = copyImage.src;
				imageElements[i].alt = copyImage.alt;
				key.className = 'copyCode';
			}
		}
	}
}

function CopyCode_CheckKey(key, eventObj)
{
	if(eventObj.keyCode == 13)
		CopyCode(key);
}

/*
**********
**********   End Copy Code
**********
*/
