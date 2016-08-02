<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0"
		xmlns:ddue="http://ddue.schemas.microsoft.com/authoring/2003/5"
		xmlns:xlink="http://www.w3.org/1999/xlink"
		xmlns:MSHelp="http://msdn.microsoft.com/mshelp"
>

	<!-- sections -->

	<xsl:template match="ddue:remarks">
		<xsl:if test="normalize-space(.)">
			<xsl:call-template name="section">
				<xsl:with-param name="title">
					<include item="remarksTitle" />
				</xsl:with-param>
				<xsl:with-param name="content">
					<xsl:apply-templates />
				</xsl:with-param>
			</xsl:call-template>
		</xsl:if>
	</xsl:template>

	<xsl:template match="ddue:codeExamples">
		<xsl:if test="normalize-space(.)">
			<xsl:call-template name="section">
				<xsl:with-param name="title">
					<include item="examplesTitle" />
				</xsl:with-param>
				<xsl:with-param name="content">
					<xsl:apply-templates />
				</xsl:with-param>
			</xsl:call-template>
		</xsl:if>
	</xsl:template>

	<xsl:template match="ddue:threadSafety">
		<xsl:call-template name="section">
			<xsl:with-param name="title">
				<include item="threadSafetyTitle" />
			</xsl:with-param>
			<xsl:with-param name="content">
				<xsl:apply-templates />
			</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<xsl:template match="ddue:notesForImplementers">
		<xsl:call-template name="section">
			<xsl:with-param name="title">
				<include item="notesForImplementersTitle" />
			</xsl:with-param>
			<xsl:with-param name="content">
				<xsl:apply-templates />
			</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<xsl:template match="ddue:notesForCallers">
		<xsl:call-template name="section">
			<xsl:with-param name="title">
				<include item="notesForCallersTitle" />
			</xsl:with-param>
			<xsl:with-param name="content">
				<xsl:apply-templates />
			</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<xsl:template match="ddue:schemaHierarchy">
		<xsl:for-each select="ddue:link">
			<xsl:call-template name="indent">
				<xsl:with-param name="count" select="position()"/>
			</xsl:call-template>
			<xsl:apply-templates select="."/>
			<br/>
		</xsl:for-each>
	</xsl:template>

	<!-- indent by 2*n spaces -->
	<xsl:template name="indent">
		<xsl:param name="count" />
		<xsl:if test="$count &gt; 1">
			<xsl:text>&#160;&#160;</xsl:text>
			<xsl:call-template name="indent">
				<xsl:with-param name="count" select="$count - 1" />
			</xsl:call-template>
		</xsl:if>
	</xsl:template>

	<xsl:template match="ddue:syntaxSection">
		<xsl:call-template name="section">
			<xsl:with-param name="title">
				<include item="syntaxTitle" />
			</xsl:with-param>
			<xsl:with-param name="content">
				<xsl:apply-templates />
			</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<xsl:template match="ddue:legacySyntax">
		<pre xml:space="preserve"><xsl:copy-of select="."/></pre>
	</xsl:template>

	<xsl:template match="ddue:relatedTopics">
		<xsl:if test="count(*) &gt; 0">
			<xsl:call-template name="section">
				<xsl:with-param name="title">
					<include item="relatedTitle" />
				</xsl:with-param>
				<xsl:with-param name="content">
					<xsl:for-each select="ddue:codeEntityReference|ddue:link|ddue:legacyLink|ddue:externalLink">
						<xsl:apply-templates select="." />
						<br />
					</xsl:for-each>
				</xsl:with-param>
			</xsl:call-template>
		</xsl:if>
	</xsl:template>

	<!-- just skip over these -->
	<xsl:template match="ddue:content | ddue:codeExample | ddue:legacy">
		<xsl:apply-templates />
	</xsl:template>

	<!-- block elements -->

	<xsl:template match="ddue:table">
		<xsl:if test="normalize-space(ddue:title)">
			<div class="caption">
				<xsl:value-of select="ddue:title"/>
			</div>
		</xsl:if>
		<table class="authoredTable">
			<xsl:apply-templates />
		</table>
	</xsl:template>

	<xsl:template match="ddue:tableHeader">
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="ddue:row">
		<tr>
			<xsl:apply-templates />
		</tr>
	</xsl:template>

	<xsl:template match="ddue:entry">
		<td>
			<xsl:apply-templates select="@address" />
			<xsl:apply-templates />
		</td>
	</xsl:template>

	<xsl:template match="ddue:tableHeader/ddue:row/ddue:entry">
		<th>
			<xsl:apply-templates />
		</th>
	</xsl:template>

	<xsl:template match="ddue:definitionTable">
		<dl class="authored">
			<xsl:apply-templates />
		</dl>
	</xsl:template>

	<xsl:template match="ddue:definedTerm">
		<dt>
			<xsl:apply-templates select="@address" />
			<xsl:apply-templates />
		</dt>
	</xsl:template>

	<xsl:template match="ddue:definition">
		<dd>
			<xsl:apply-templates />
		</dd>
	</xsl:template>

	<xsl:template match="ddue:code">
		<xsl:variable name="codeLang">
			<xsl:call-template name="getCodeLang">
				<xsl:with-param name="p_codeLang" select="@language" />
			</xsl:call-template>
		</xsl:variable>
		<div class="code">
			<table width="100%" cellspacing="0" cellpadding="0">
				<tr>
					<th>
						<xsl:choose>
							<xsl:when test="@title">
								<xsl:value-of select="@title" />
							</xsl:when>
							<xsl:when test="$codeLang != 'other' and $codeLang != 'none'">
								<include item="{$codeLang}"/>
							</xsl:when>
						</xsl:choose>
						<xsl:text>&#xa0;</xsl:text>
					</th>
					<th>
						<span class="copyCode" onclick="CopyCode(this)" onkeypress="CopyCode_CheckKey(this, event)" onmouseover="ChangeCopyCodeIcon(this)" onmouseout="ChangeCopyCodeIcon(this)" tabindex="0">
							<img class="copyCodeImage" name="ccImage" align="absmiddle">
								<includeAttribute name="alt" item="copyImage" />
								<includeAttribute name="title" item="copyImage" />
								<includeAttribute name="src" item="iconPath">
									<parameter>copycode.gif</parameter>
								</includeAttribute>
							</img>
							<include item="copyCode"/>
						</span>
					</th>
				</tr>
				<tr>
					<td colspan="2">
						<pre xml:space="preserve"><xsl:apply-templates/></pre>
					</td>
				</tr>
			</table>
		</div>
	</xsl:template>

	<xsl:template match="ddue:sampleCode">
		<div>
			<b>
				<xsl:value-of select="@language"/>
			</b>
		</div>
		<div class="code">
			<pre xml:space="preserve"><xsl:apply-templates /></pre>
		</div>
	</xsl:template>

	<xsl:template name="composeCode">
		<xsl:copy-of select="." />
		<xsl:variable name="next" select="following-sibling::*[1]" />
		<xsl:if test="boolean($next/@language) and boolean(local-name($next)=local-name())">
			<xsl:for-each select="$next">
				<xsl:call-template name="composeCode" />
			</xsl:for-each>
		</xsl:if>
	</xsl:template>

	<xsl:template match="ddue:alert">
		<xsl:variable name="title">
			<xsl:choose>
				<xsl:when test="@class='note'">
					<xsl:text>noteTitle</xsl:text>
				</xsl:when>
				<xsl:when test="@class='tip'">
					<xsl:text>tipTitle</xsl:text>
				</xsl:when>
				<xsl:when test="@class='caution' or @class='warning'">
					<xsl:text>cautionTitle</xsl:text>
				</xsl:when>
				<xsl:when test="@class='security' or @class='security note'">
					<xsl:text>securityTitle</xsl:text>
				</xsl:when>
				<xsl:when test="@class='important'">
					<xsl:text>importantTitle</xsl:text>
				</xsl:when>
				<xsl:when test="@class='vb' or @class='VB' or @class='VisualBasic' or @class='visual basic note'">
					<xsl:text>visualBasicTitle</xsl:text>
				</xsl:when>
				<xsl:when test="@class='cs' or @class='CSharp' or @class='c#' or @class='C#' or @class='visual c# note'">
					<xsl:text>visualC#Title</xsl:text>
				</xsl:when>
				<xsl:when test="@class='cpp' or @class='c++' or @class='C++' or @class='CPP' or @class='visual c++ note'">
					<xsl:text>visualC++Title</xsl:text>
				</xsl:when>
				<xsl:when test="@class='JSharp' or @class='j#' or @class='J#' or @class='visual j# note'">
					<xsl:text>visualJ#Title</xsl:text>
				</xsl:when>
				<xsl:when test="@class='implement'">
					<xsl:text>NotesForImplementers</xsl:text>
				</xsl:when>
				<xsl:when test="@class='caller'">
					<xsl:text>NotesForCallers</xsl:text>
				</xsl:when>
				<xsl:when test="@class='inherit'">
					<xsl:text>NotesForInheritors</xsl:text>
				</xsl:when>
				<xsl:otherwise>
					<xsl:text>noteTitle</xsl:text>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="altTitle">
			<xsl:choose>
				<xsl:when test="@class='note' or @class='implement' or @class='caller' or @class='inherit'">
					<xsl:text>noteAltText</xsl:text>
				</xsl:when>
				<xsl:when test="@class='tip'">
					<xsl:text>tipAltText</xsl:text>
				</xsl:when>
				<xsl:when test="@class='caution' or @class='warning'">
					<xsl:text>cautionAltText</xsl:text>
				</xsl:when>
				<xsl:when test="@class='security' or @class='security note'">
					<xsl:text>securityAltText</xsl:text>
				</xsl:when>
				<xsl:when test="@class='important'">
					<xsl:text>importantAltText</xsl:text>
				</xsl:when>
				<xsl:when test="@class='vb' or @class='VB' or @class='VisualBasic' or @class='visual basic note'">
					<xsl:text>visualBasicAltText</xsl:text>
				</xsl:when>
				<xsl:when test="@class='cs' or @class='CSharp' or @class='c#' or @class='C#' or @class='visual c# note'">
					<xsl:text>visualC#AltText</xsl:text>
				</xsl:when>
				<xsl:when test="@class='cpp' or @class='c++' or @class='C++' or @class='CPP' or @class='visual c++ note'">
					<xsl:text>visualC++AltText</xsl:text>
				</xsl:when>
				<xsl:when test="@class='JSharp' or @class='j#' or @class='J#' or @class='visual j# note'">
					<xsl:text>visualJ#AltText</xsl:text>
				</xsl:when>
				<xsl:otherwise>
					<xsl:text>noteAltText</xsl:text>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="noteImg">
			<xsl:choose>
				<xsl:when test="@class='note' or @class='tip' or @class='implement' or @class='caller' or @class='inherit'">
					<xsl:text>alert_note.gif</xsl:text>
				</xsl:when>
				<xsl:when test="@class='caution' or @class='warning'">
					<xsl:text>alert_caution.gif</xsl:text>
				</xsl:when>
				<xsl:when test="@class='security' or @class='security note'">
					<xsl:text>alert_security.gif</xsl:text>
				</xsl:when>
				<xsl:when test="@class='important'">
					<xsl:text>alert_caution.gif</xsl:text>
				</xsl:when>
				<xsl:when test="@class='vb' or @class='VB' or @class='VisualBasic' or @class='visual basic note'">
					<xsl:text>alert_note.gif</xsl:text>
				</xsl:when>
				<xsl:when test="@class='cs' or @class='CSharp' or @class='c#' or @class='C#' or @class='visual c# note'">
					<xsl:text>alert_note.gif</xsl:text>
				</xsl:when>
				<xsl:when test="@class='cpp' or @class='c++' or @class='C++' or @class='CPP' or @class='visual c++ note'">
					<xsl:text>alert_note.gif</xsl:text>
				</xsl:when>
				<xsl:when test="@class='JSharp' or @class='j#' or @class='J#' or @class='visual j# note'">
					<xsl:text>alert_note.gif</xsl:text>
				</xsl:when>
				<xsl:otherwise>
					<xsl:text>alert_note.gif</xsl:text>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<div class="alert">
			<table>
				<tr>
					<th>
						<img>
							<includeAttribute item="iconPath" name="src">
								<parameter>
									<xsl:value-of select="$noteImg"/>
								</parameter>
							</includeAttribute>
							<includeAttribute name="title" item="{$altTitle}" />
						</img>
						<xsl:text> </xsl:text>
						<include item="{$title}" />
					</th>
				</tr>
				<tr>
					<td>
						<xsl:apply-templates />
					</td>
				</tr>
			</table>
		</div>
	</xsl:template>

	<xsl:template match="ddue:sections">
		<xsl:apply-templates select="ddue:section" />
	</xsl:template>

	<xsl:template match="ddue:section">
		<xsl:apply-templates select="@address" />
		<xsl:call-template name="section">
			<xsl:with-param name="title">
				<xsl:value-of select="ddue:title" />
			</xsl:with-param>
			<xsl:with-param name="content">
				<div class="subsection">
					<xsl:apply-templates select="ddue:content"/>
					<xsl:apply-templates select="ddue:sections" />
				</div>
			</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<xsl:template match="ddue:mediaLink">
		<div>
			<xsl:choose>
				<xsl:when test="ddue:image[@placement='center']">
					<xsl:attribute name="class">mediaCenter</xsl:attribute>
				</xsl:when>
				<xsl:when test="ddue:image[@placement='far']">
					<xsl:attribute name="class">mediaFar</xsl:attribute>
				</xsl:when>
				<xsl:otherwise>
					<xsl:attribute name="class">mediaNear</xsl:attribute>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:if test="ddue:caption and not(ddue:caption[@placement='after'])">
				<div class="caption">
					<xsl:if test="ddue:caption[@lead]">
						<span class="captionLead">
							<xsl:value-of select="normalize-space(ddue:caption/@lead)" />:
						</span>
					</xsl:if>
					<xsl:apply-templates select="ddue:caption" />
				</div>
			</xsl:if>
			<artLink target="{ddue:image/@xlink:href}" />
			<xsl:if test="ddue:caption and ddue:caption[@placement='after']">
				<div class="caption">
					<xsl:if test="ddue:caption[@lead]">
						<span class="captionLead">
							<xsl:value-of select="normalize-space(ddue:caption/@lead)" />:
						</span>
					</xsl:if>
					<xsl:apply-templates select="ddue:caption" />
				</div>
			</xsl:if>
		</div>
	</xsl:template>

	<xsl:template match="ddue:mediaLinkInline">
		<span class="media">
			<artLink target="{ddue:image/@xlink:href}" />
		</span>
	</xsl:template>

	<xsl:template match="ddue:procedure">
		<xsl:apply-templates select="@address" />
		<xsl:call-template name="section">
			<xsl:with-param name="title">
				<xsl:value-of select="ddue:title" />
			</xsl:with-param>
			<xsl:with-param name="content">
				<xsl:apply-templates select="ddue:steps" />
				<xsl:apply-templates select="ddue:conclusion" />
			</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<xsl:template match="ddue:steps">
		<xsl:choose>
			<xsl:when test="@class='ordered'">
				<ol>
					<xsl:apply-templates select="ddue:step" />
				</ol>
			</xsl:when>
			<xsl:when test="@class='bullet'">
				<ul>
					<xsl:apply-templates select="ddue:step" />
				</ul>
			</xsl:when>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="ddue:step">
		<li>
			<xsl:apply-templates select="@address" />
			<xsl:apply-templates />
		</li>
	</xsl:template>


	<xsl:template match="ddue:inThisSection">
		<xsl:call-template name="section">
			<xsl:with-param name="title">
				<include item="inThisSectionTitle" />
			</xsl:with-param>
			<xsl:with-param name="content">
				<xsl:apply-templates />
			</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<xsl:template match="ddue:buildInstructions">
		<xsl:call-template name="section">
			<xsl:with-param name="title">
				<include item="buildInstructionsTitle" />
			</xsl:with-param>
			<xsl:with-param name="content">
				<xsl:apply-templates />
			</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<xsl:template match="ddue:nextSteps">
		<xsl:call-template name="section">
			<xsl:with-param name="title">
				<include item="nextStepsTitle" />
			</xsl:with-param>
			<xsl:with-param name="content">
				<xsl:apply-templates />
			</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<xsl:template match="ddue:requirements">
		<xsl:call-template name="section">
			<xsl:with-param name="title">
				<include item="requirementsTitle" />
			</xsl:with-param>
			<xsl:with-param name="content">
				<xsl:apply-templates />
			</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<!-- inline elements -->

	<xsl:template match="ddue:languageKeyword">
		<xsl:variable name="word" select="." />
		<span class="keyword" sdata="langKeyword" value="{$word}">
			<xsl:choose>
				<xsl:when test="$word='null' or $word='Nothing' or $word='nullptr'">
					<span class="cs">null</span>
					<span class="vb">Nothing</span>
					<span class="cpp">nullptr</span>
				</xsl:when>
				<xsl:when test="$word='static' or $word='Shared'">
					<span class="cs">static</span>
					<span class="vb">Shared</span>
					<span class="cpp">static</span>
				</xsl:when>
				<xsl:when test="$word='virtual' or $word='Overridable'">
					<span class="cs">virtual</span>
					<span class="vb">Overridable</span>
					<span class="cpp">virtual</span>
				</xsl:when>
				<xsl:when test="$word='true' or $word='True'">
					<span class="cs">true</span>
					<span class="vb">True</span>
					<span class="cpp">true</span>
				</xsl:when>
				<xsl:when test="$word='false' or $word='False'">
					<span class="cs">false</span>
					<span class="vb">False</span>
					<span class="cpp">false</span>
				</xsl:when>
				<xsl:when test="$word='abstract' or $word='MustInherit'">
					<span class="cs">abstract</span>
					<span class="vb">MustInherit</span>
					<span class="cpp">abstract</span>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="." />
				</xsl:otherwise>
			</xsl:choose>
		</span>
	</xsl:template>

	<!-- links -->

	<xsl:template match="ddue:dynamicLink[@type='inline']">
		<MSHelp:ktable disambiguator='span' indexMoniker='!DefaultDynamicLinkIndex'>
			<xsl:attribute name="keywords">
				<xsl:for-each select="ddue:keyword">
					<xsl:value-of select="."/>
					<xsl:if test="position() != last()">;</xsl:if>
				</xsl:for-each>
			</xsl:attribute>
			<includeAttribute name="prefix" item="dynamicLinkInlinePreFixText" />
			<includeAttribute name="postfix" item="dynamicLinkInlinePostFixText" />
			<includeAttribute name="separator" item="dynamicLinkInlineSeperatorText" />
		</MSHelp:ktable>
	</xsl:template>

	<xsl:template match="ddue:dynamicLink[@type='table']">
		<include item="mshelpKTable">
			<parameter>
				<xsl:for-each select="ddue:keyword">
					<xsl:value-of select="."/>
					<xsl:if test="position() != last()">;</xsl:if>
				</xsl:for-each>
			</parameter>
		</include>
	</xsl:template>

	<xsl:template match="ddue:dynamicLink[@type='bulleted']">
		<MSHelp:ktable disambiguator='span' indexMoniker='!DefaultDynamicLinkIndex'>
			<xsl:attribute name="keywords">
				<xsl:for-each select="ddue:keyword">
					<xsl:value-of select="."/>
					<xsl:if test="position() != last()">;</xsl:if>
				</xsl:for-each>
			</xsl:attribute>
			<xsl:attribute name="prefix">&lt;ul&gt;&lt;li&gt;</xsl:attribute>
			<xsl:attribute name="postfix">&lt;/li&gt;&lt;/ul&gt;</xsl:attribute>
			<xsl:attribute name="separator">&lt;/li&gt;&lt;li&gt;</xsl:attribute>
		</MSHelp:ktable>
	</xsl:template>

	<xsl:template match="ddue:codeFeaturedElement">
		<xsl:if test="normalize-space(.)">
			<b>
				<xsl:apply-templates/>
			</b>
		</xsl:if>
	</xsl:template>

	<xsl:template match="ddue:languageReferenceRemarks">
		<xsl:call-template name="section">
			<xsl:with-param name="title">
				<include item="remarksTitle" />
			</xsl:with-param>
			<xsl:with-param name="content">
				<xsl:apply-templates />
			</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<xsl:template match="ddue:attributesandElements">
		<xsl:call-template name="section">
			<xsl:with-param name="title">
				<include item="attributesAndElements" />
			</xsl:with-param>
			<xsl:with-param name="content">
				<xsl:apply-templates />
			</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<xsl:template match="ddue:attributes">
		<h4 class="subHeading">
			<include item="attributes"/>
		</h4>
		<xsl:apply-templates/>
	</xsl:template>

	<xsl:template match="ddue:attribute">
		<xsl:apply-templates/>
	</xsl:template>

	<xsl:template match="ddue:attribute/ddue:title">
		<h4 class="subHeading">
			<xsl:apply-templates/>
		</h4>
	</xsl:template>

	<xsl:template match="ddue:childElement">
		<h4 class="subHeading">
			<include item="childElement"/>
		</h4>
		<xsl:apply-templates/>
	</xsl:template>

	<xsl:template match="ddue:parentElement">
		<h4 class="subHeading">
			<include item="parentElement"/>
		</h4>
		<xsl:apply-templates/>
	</xsl:template>

	<xsl:template match="ddue:textValue">
		<xsl:call-template name="section">
			<xsl:with-param name="title">
				<include item="textValue" />
			</xsl:with-param>
			<xsl:with-param name="content">
				<xsl:apply-templates />
			</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<xsl:template match="ddue:elementInformation">
		<xsl:call-template name="section">
			<xsl:with-param name="title">
				<include item="elementInformation" />
			</xsl:with-param>
			<xsl:with-param name="content">
				<xsl:apply-templates />
			</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<xsl:template match="ddue:dotNetFrameworkEquivalent">
		<xsl:call-template name="section">
			<xsl:with-param name="title">
				<include item="dotNetFrameworkEquivalent" />
			</xsl:with-param>
			<xsl:with-param name="content">
				<xsl:apply-templates />
			</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<xsl:template match="ddue:prerequisites">
		<xsl:call-template name="section">
			<xsl:with-param name="title">
				<include item="prerequisites" />
			</xsl:with-param>
			<xsl:with-param name="content">
				<xsl:apply-templates />
			</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<xsl:template match="ddue:type">
		<xsl:apply-templates/>
	</xsl:template>

	<xsl:template match="ddue:robustProgramming">
		<xsl:call-template name="section">
			<xsl:with-param name="title">
				<include item="robustProgramming" />
			</xsl:with-param>
			<xsl:with-param name="content">
				<xsl:apply-templates />
			</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<xsl:template match="ddue:security">
		<xsl:call-template name="section">
			<xsl:with-param name="title">
				<include item="securitySection" />
			</xsl:with-param>
			<xsl:with-param name="content">
				<xsl:apply-templates />
			</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<xsl:template match="ddue:externalResources">
		<xsl:call-template name="section">
			<xsl:with-param name="title">
				<include item="externalResources" />
			</xsl:with-param>
			<xsl:with-param name="content">
				<xsl:apply-templates />
			</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<xsl:template match="ddue:demonstrates">
		<xsl:call-template name="section">
			<xsl:with-param name="title">
				<include item="demonstrates" />
			</xsl:with-param>
			<xsl:with-param name="content">
				<xsl:apply-templates />
			</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<xsl:template match="ddue:appliesTo">
		<xsl:call-template name="section">
			<xsl:with-param name="title">
				<include item="appliesTo" />
			</xsl:with-param>
			<xsl:with-param name="content">
				<xsl:apply-templates />
			</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<xsl:template match="ddue:conclusion">
		<xsl:call-template name="section">
			<xsl:with-param name="title">
				<include item="conclusion" />
			</xsl:with-param>
			<xsl:with-param name="content">
				<xsl:apply-templates />
			</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<xsl:template match="ddue:background">
		<xsl:call-template name="section">
			<xsl:with-param name="title">
				<include item="background" />
			</xsl:with-param>
			<xsl:with-param name="content">
				<xsl:apply-templates />
			</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<xsl:template match="ddue:whatsNew">
		<xsl:call-template name="section">
			<xsl:with-param name="title">
				<include item="whatsNew" />
			</xsl:with-param>
			<xsl:with-param name="content">
				<xsl:apply-templates />
			</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<xsl:template name="createReferenceLink">
		<xsl:param name="id" />
		<xsl:param name="qualified" select="false()" />
		<referenceLink target="{$id}" qualified="{$qualified}" />
	</xsl:template>

	<!-- this is temporary -->
	<xsl:template match="ddue:snippets">
		<xsl:variable name="codeId" select="generate-id()" />
		<div name="snippetGroup">
			<table class="filter">
				<tr class="tabs" id="ct_{$codeId}">
					<xsl:for-each select="ddue:snippet">
						<td class="tab" x-lang="{@language}" onclick="toggleClass('ct_{$codeId}','x-lang','{@language}','activeTab','tab'); toggleStyle('cb_{$codeId}','x-lang','{@language}','display','block','none');">
							<include item="{@language}Label" />
						</td>
					</xsl:for-each>
				</tr>
			</table>
			<div id="cb_{$codeId}">
				<xsl:for-each select="ddue:snippet">
					<div class="code" x-lang="{@language}">
						<pre xml:space="preserve"><xsl:copy-of select="node()" /></pre>
					</div>
				</xsl:for-each>
			</div>
		</div>
	</xsl:template>

	<xsl:template name="section">
		<xsl:param name="title" />
		<xsl:param name="content" />
		<div class="section">
			<div class="sectionTitle" onclick="toggleSection(this.parentNode)">
				<img>
					<includeAttribute name="src" item="iconPath">
						<parameter>collapse_all.gif</parameter>
					</includeAttribute>
				</img>
				<xsl:text> </xsl:text>
				<xsl:copy-of select="$title" />
			</div>
			<div class="sectionContent">
				<xsl:copy-of select="$content" />
			</div>
		</div>
	</xsl:template>

	<!-- sections -->

	<xsl:template match="ddue:summary">
		<xsl:if test="not(@abstract='true')">
			<!-- The ddue:summary element is redundant since it's optional in
           the MAML schema but ddue:introduction is not.  Using abstract='true'
           will prevent the summary from being included in the topic; however,
           the first child para element in the summary will still be used as
           the abstract for the topic's Help 2.x metadata. -->
			<div class="summary">
				<xsl:apply-templates />
			</div>
		</xsl:if>
	</xsl:template>

	<xsl:template match="@address">
		<a name="{string(.)}">
			<xsl:text> </xsl:text>
		</a>
	</xsl:template>

	<!-- block elements -->

	<xsl:template match="ddue:para">
		<p>
			<xsl:apply-templates />
		</p>
	</xsl:template>

	<xsl:template match="ddue:list">
		<xsl:choose>
			<xsl:when test="@class='bullet'">
				<ul>
					<xsl:apply-templates select="ddue:listItem" />
				</ul>
			</xsl:when>
			<xsl:when test="@class='ordered'">
				<ol>
					<xsl:if test="@start">
						<xsl:attribute name="start">
							<xsl:value-of select="@start"/>
						</xsl:attribute>
					</xsl:if>
					<xsl:apply-templates select="ddue:listItem" />
				</ol>
			</xsl:when>
			<xsl:otherwise>
				<ul class="nobullet">
					<xsl:apply-templates select="ddue:listItem" />
				</ul>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="ddue:listItem">
		<li>
			<xsl:apply-templates select="@address" />
			<xsl:apply-templates />
		</li>
	</xsl:template>

	<!-- inline elements -->

	<xsl:template match="ddue:parameterReference">
		<xsl:if test="normalize-space(.)">
			<span class="parameter" sdata="paramReference">
				<xsl:value-of select="." />
			</span>
		</xsl:if>
	</xsl:template>

	<xsl:template match="ddue:ui">
		<xsl:if test="normalize-space(.)">
			<span class="ui">
				<xsl:value-of select="." />
			</span>
		</xsl:if>
	</xsl:template>

	<xsl:template match="ddue:userInput | ddue:userInputLocalizable">
		<xsl:if test="normalize-space(.)">
			<span class="input">
				<xsl:value-of select="." />
			</span>
		</xsl:if>
	</xsl:template>

	<xsl:template match="ddue:newTerm">
		<xsl:if test="normalize-space(.)">
			<span class="term">
				<xsl:value-of select="." />
			</span>
		</xsl:if>
	</xsl:template>

	<xsl:template match="ddue:math">
		<xsl:if test="normalize-space(.)">
			<span class="math">
				<xsl:apply-templates/>
			</span>
		</xsl:if>
	</xsl:template>

	<xsl:template match="ddue:command">
		<xsl:if test="normalize-space(.)">
			<span class="command">
				<xsl:apply-templates />
			</span>
		</xsl:if>
	</xsl:template>

	<xsl:template match="ddue:replaceable">
		<xsl:if test="normalize-space(.)">
			<span class="parameter">
				<xsl:apply-templates />
			</span>
		</xsl:if>
	</xsl:template>

	<xsl:template match="ddue:literal">
		<xsl:if test="normalize-space(.)">
			<span class="literalValue">
				<xsl:apply-templates />
			</span>
		</xsl:if>
	</xsl:template>

	<xsl:template match="ddue:codeInline|ddue:computerOutputInline|ddue:environmentVariable">
		<xsl:if test="normalize-space(.)">
			<span class="code">
				<xsl:value-of select="." />
			</span>
		</xsl:if>
	</xsl:template>

	<xsl:template match="ddue:subscript | ddue:subscriptType">
		<xsl:if test="normalize-space(.)">
			<sub>
				<xsl:value-of select="." />
			</sub>
		</xsl:if>
	</xsl:template>

	<xsl:template match="ddue:superscript | ddue:superscriptType">
		<xsl:if test="normalize-space(.)">
			<sup>
				<xsl:value-of select="." />
			</sup>
		</xsl:if>
	</xsl:template>

	<xsl:template match="ddue:legacyBold">
		<xsl:if test="normalize-space(.)">
			<b>
				<xsl:apply-templates />
			</b>
		</xsl:if>
	</xsl:template>

	<xsl:template match="ddue:legacyItalic">
		<xsl:if test="normalize-space(.)">
			<i>
				<xsl:apply-templates />
			</i>
		</xsl:if>
	</xsl:template>

	<xsl:template match="ddue:legacyUnderline">
		<xsl:if test="normalize-space(.)">
			<u>
				<xsl:apply-templates />
			</u>
		</xsl:if>
	</xsl:template>

	<xsl:template match="ddue:embeddedLabel">
		<xsl:if test="normalize-space(.)">
			<span class="label">
				<xsl:apply-templates/>
			</span>
		</xsl:if>
	</xsl:template>

	<xsl:template match="ddue:errorInline|ddue:fictitiousUri|ddue:localUri">
		<xsl:if test="normalize-space(.)">
			<span class="italic">
				<xsl:value-of select="." />
			</span>
		</xsl:if>
	</xsl:template>

	<xsl:template match="ddue:quote">
		<xsl:if test="normalize-space(.)">
			<blockQuote>
				<xsl:apply-templates/>
			</blockQuote>
		</xsl:if>
	</xsl:template>

	<xsl:template match="ddue:quoteInline">
		<xsl:if test="normalize-space(.)">
			<q>
				<xsl:apply-templates/>
			</q>
		</xsl:if>
	</xsl:template>

	<xsl:template match="ddue:date">
		<xsl:apply-templates/>
	</xsl:template>

	<xsl:template match="ddue:foreignPhrase">
		<xsl:if test="normalize-space(.)">
			<span class="foreignPhrase">
				<xsl:apply-templates/>
			</span>
		</xsl:if>
	</xsl:template>

	<xsl:template match="ddue:phrase">
		<xsl:if test="normalize-space(.)">
			<span class="phrase">
				<xsl:apply-templates/>
			</span>
		</xsl:if>
	</xsl:template>

	<xsl:template match="ddue:system|ddue:hardware|ddue:application|ddue:database">
		<xsl:if test="normalize-space(.)">
			<b>
				<xsl:apply-templates/>
			</b>
		</xsl:if>
	</xsl:template>

	<xsl:template match="ddue:placeholder">
		<xsl:if test="normalize-space(.)">
			<span class="placeholder">
				<xsl:apply-templates/>
			</span>
		</xsl:if>
	</xsl:template>

	<xsl:template match="ddue:copyright">
		<!-- <p>{0} &copy;{1}{2}. All rights reserved.</p> -->
		<include item="copyrightNotice">
			<parameter>
				<xsl:value-of select="ddue:trademark" />
			</parameter>
			<parameter>
				<xsl:for-each select="ddue:year">
					<xsl:if test="position() = 1">
						<xsl:text> </xsl:text>
					</xsl:if>
					<xsl:value-of select="."/>
					<xsl:if test="position() != last()">
						<xsl:text>, </xsl:text>
					</xsl:if>
				</xsl:for-each>
			</parameter>
			<parameter>
				<xsl:for-each select="ddue:holder">
					<xsl:if test="position() = 1">
						<xsl:text> </xsl:text>
					</xsl:if>
					<xsl:value-of select="."/>
					<xsl:if test="position() != last()">
						<xsl:text>, </xsl:text>
					</xsl:if>
				</xsl:for-each>
			</parameter>
		</include>
	</xsl:template>

	<xsl:template match="ddue:corporation">
		<xsl:apply-templates/>
	</xsl:template>

	<xsl:template match="ddue:country">
		<xsl:apply-templates/>
	</xsl:template>

	<xsl:template match="ddue:unmanagedCodeEntityReference">
		<xsl:if test="normalize-space(.)">
			<b>
				<xsl:apply-templates/>
			</b>
		</xsl:if>
	</xsl:template>

	<xsl:template match="ddue:localizedText">
		<xsl:apply-templates/>
	</xsl:template>

	<!-- links -->

	<xsl:template match="ddue:externalLink">
		<a>
			<xsl:attribute name="href">
				<xsl:value-of select="normalize-space(ddue:linkUri)" />
			</xsl:attribute>
			<xsl:if test="normalize-space(ddue:linkAlternateText)">
				<xsl:attribute name="title">
					<xsl:value-of select="normalize-space(ddue:linkAlternateText)" />
				</xsl:attribute>
			</xsl:if>
			<xsl:attribute name="target">
				<xsl:choose>
					<xsl:when test="normalize-space(ddue:linkTarget)">
						<xsl:value-of select="normalize-space(ddue:linkTarget)" />
					</xsl:when>
					<xsl:otherwise>
						<xsl:text>_blank</xsl:text>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>
			<xsl:value-of select="normalize-space(ddue:linkText)" />
		</a>
	</xsl:template>

	<xsl:template match="ddue:link">
		<span sdata="link">
			<xsl:choose>
				<xsl:when test="starts-with(@xlink:href,'#')">
					<!-- in-page link -->
					<a href="{@xlink:href}">
						<xsl:apply-templates />
					</a>
				</xsl:when>
				<xsl:otherwise>
					<!-- verified, external link -->
					<conceptualLink target="{@xlink:href}">
						<xsl:apply-templates />
					</conceptualLink>
				</xsl:otherwise>
			</xsl:choose>
		</span>
	</xsl:template>

	<xsl:template match="ddue:legacyLink">
		<a href="{@xlink:href}">
			<xsl:apply-templates />
		</a>
	</xsl:template>

	<xsl:template match="ddue:codeEntityReference">
		<span sdata="cer" target="{normalize-space(string(.))}">
			<referenceLink target="{normalize-space(string(.))}">
				<xsl:if test="@qualifyHint">
					<xsl:attribute name="show-container">
						<xsl:value-of select="@qualifyHint" />
					</xsl:attribute>
					<xsl:attribute name="show-parameters">
						<xsl:value-of select="@qualifyHint" />
					</xsl:attribute>
				</xsl:if>
				<xsl:if test="@autoUpgrade">
					<xsl:attribute name="prefer-overload">
						<xsl:value-of select="@autoUpgrade" />
					</xsl:attribute>
				</xsl:if>
				<xsl:if test="normalize-space(@linkText)">
					<xsl:value-of select="normalize-space(@linkText)"/>
				</xsl:if>
			</referenceLink>
		</span>
	</xsl:template>
	<!-- capture authored glossary <link> nodes -->
	<!-- LEAVE THIS TEMPORARILY to support oldstyle GTMT link tagging -->
	<xsl:template match="ddue:link[starts-with(.,'GTMT#')]">
		<!-- not supporting popup definitions; just show the display text -->
		<span sdata="link">
			<xsl:value-of select="substring-after(.,'GTMT#')"/>
		</span>
	</xsl:template>

	<!-- capture authored glossary <link> nodes -->
	<!-- THIS IS THE NEW STYLE GTMT link tagging -->
	<xsl:template match="ddue:legacyLink[starts-with(@xlink:href,'GTMT#')]">
		<!-- not supporting popup definitions; just show the display text -->
		<xsl:value-of select="."/>
	</xsl:template>

	<!-- Glossary document type support -->
	<xsl:variable name="allUpperCaseLetters">ABCDEFGHIJKLMNOPQRSTUVWXYZ</xsl:variable>
	<xsl:variable name="allLowerCaseLetters">abcdefghijklmnopqrstuvwxyz</xsl:variable>

	<xsl:key name="glossaryTermFirstLetters" match="//ddue:glossaryEntry"
           use="translate(substring(ddue:terms/ddue:term/text(),1,1),'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ ')"/>

	<xsl:template match="ddue:glossary">
		<xsl:if test="ddue:title">
			<h1 class="glossaryTitle">
				<xsl:value-of select="normalize-space(ddue:title)" />
			</h1>
		</xsl:if>
		<xsl:choose>
			<xsl:when test="ddue:glossaryDiv">
				<!-- Organized glossary with glossaryDiv elements -->
				<br/>
				<xsl:for-each select="ddue:glossaryDiv">
					<xsl:if test="ddue:title">
						<xsl:choose>
							<xsl:when test="@address">
								<a>
									<!-- Keep this on one line or the spaces preceeding the "#" end up in the anchor name -->
									<xsl:attribute name="href">#<xsl:value-of select="@address"/></xsl:attribute>
									<xsl:value-of select="ddue:title" />
								</a>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="ddue:title" />
							</xsl:otherwise>
						</xsl:choose>
					</xsl:if>
					<xsl:if test="position() != last()">
						<xsl:text> | </xsl:text>
					</xsl:if>
				</xsl:for-each>

				<xsl:apply-templates select="ddue:glossaryDiv"/>
			</xsl:when>
			<xsl:otherwise>
				<!-- Simple glossary consisting of nothing by glossaryEntry elements -->
				<br/>
				<xsl:call-template name="glossaryLetterBar"/>
				<br/>
				<xsl:call-template name="glossaryGroupByEntriesTermFirstLetter"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="ddue:glossaryDiv">
		<xsl:if test="@address">
			<a>
				<!-- Keep this on one line or the spaces preceding the address end up in the anchor name -->
				<xsl:attribute name="name"><xsl:value-of select="@address"/></xsl:attribute>
				<xsl:text> </xsl:text>
			</a>
		</xsl:if>
		<div class="glossaryDiv">
			<xsl:if test="ddue:title">
				<h2 class="glossaryDivHeading">
					<xsl:value-of select="ddue:title"/>
				</h2>
			</xsl:if>
			<hr class="glossaryRule"/>
			<xsl:call-template name="glossaryLetterBar">
				<xsl:with-param name="sectionPrefix" select="generate-id()"/>
			</xsl:call-template>
			<br/>
			<xsl:call-template name="glossaryGroupByEntriesTermFirstLetter">
				<xsl:with-param name="sectionPrefix" select="generate-id()"/>
			</xsl:call-template>
		</div>
	</xsl:template>

	<xsl:template name="glossaryGroupByEntriesTermFirstLetter">
		<xsl:param name="sectionPrefix" select="''"/>
		<xsl:variable name="div" select="."/>
		<!-- Group entries by the first letter of their terms using the Muenchian method.
         http://www.jenitennison.com/xslt/grouping/muenchian.html -->
		<xsl:for-each select="ddue:glossaryEntry[generate-id() = 
                  generate-id(key('glossaryTermFirstLetters',
                  translate(substring(ddue:terms/ddue:term[1]/text(),1,1),$allLowerCaseLetters,concat($allUpperCaseLetters, ' ')))
                  [parent::node() = $div][1])]">
			<xsl:sort select="ddue:terms/ddue:term[1]" />
			<xsl:variable name="letter"
                    select="translate(substring(ddue:terms/ddue:term[1]/text(),1,1),$allLowerCaseLetters,concat($allUpperCaseLetters, ' '))"/>

			<xsl:call-template name="glossaryEntryGroup">
				<xsl:with-param name="link" select="concat($sectionPrefix,$letter)"/>
				<xsl:with-param name="name" select="$letter"/>
				<xsl:with-param name="nodes" select="key('glossaryTermFirstLetters',
                        translate($letter,$allLowerCaseLetters,concat($allUpperCaseLetters, ' ')))
                        [parent::node() = $div]"/>
			</xsl:call-template>
		</xsl:for-each>
	</xsl:template>

	<xsl:template name="glossaryEntryGroup">
		<xsl:param name="link"/>
		<xsl:param name="name"/>
		<xsl:param name="nodes"/>
		<div class="glossaryGroup">
			<a>
				<xsl:attribute name="name">
					<xsl:value-of select="$link"/>
				</xsl:attribute>
				<xsl:text> </xsl:text>
			</a>
			<h3 class="glossaryGroupHeading">
				<xsl:value-of select="$name"/>
			</h3>
			<dl class="glossaryGroupList">
				<xsl:apply-templates select="$nodes">
					<xsl:sort select="ddue:terms/ddue:term"/>
				</xsl:apply-templates>
			</dl>
		</div>
	</xsl:template>

	<xsl:template match="ddue:glossaryEntry">
		<dt class="glossaryEntry">
			<xsl:apply-templates select="@address" />
			<xsl:for-each select="ddue:terms/ddue:term">
				<xsl:sort select="normalize-space(.)" />

				<xsl:if test="@termId">
					<a>
						<!-- Keep this on one line or the spaces preceding the address end up in the anchor name -->
						<xsl:attribute name="name"><xsl:value-of select="@termId"/></xsl:attribute>
						<xsl:text> </xsl:text>
					</a>
				</xsl:if>

				<xsl:value-of select="normalize-space(.)" />
				<xsl:if test="position() != last()">
					<xsl:text>, </xsl:text>
				</xsl:if>
			</xsl:for-each>
		</dt>
		<dd class="glossaryEntry">
			<xsl:apply-templates select="ddue:definition/*"/>

			<xsl:if test="ddue:relatedEntry">
				<div class="relatedEntry">
					<include item="relatedEntries" />&#160;

					<xsl:for-each select="ddue:relatedEntry">
						<xsl:variable name="id" select="@termId" />
						<a>
							<!-- Keep this on one line or the spaces preceeding the address end up in the anchor name -->
							<xsl:attribute name="href">#<xsl:value-of select="@termId"/></xsl:attribute>
							<xsl:value-of select="//ddue:term[@termId=$id]"/>
						</a>
						<xsl:if test="position() != last()">
							<xsl:text>, </xsl:text>
						</xsl:if>
					</xsl:for-each>
				</div>
			</xsl:if>
		</dd>
	</xsl:template>

	<xsl:template name="glossaryLetterBar">
		<xsl:param name="sectionPrefix" select="''"/>
		<div class="glossaryLetterBar">
			<xsl:call-template name="glossaryLetterBarLinkRecursive">
				<xsl:with-param name="sectionPrefix" select="$sectionPrefix"/>
				<xsl:with-param name="bar" select="$allUpperCaseLetters"/>
				<xsl:with-param name="characterPosition" select="1"/>
			</xsl:call-template>
		</div>
	</xsl:template>

	<xsl:template name="glossaryLetterBarLinkRecursive">
		<xsl:param name="sectionPrefix"/>
		<xsl:param name="bar"/>
		<xsl:param name="characterPosition"/>
		<xsl:variable name="letter" select="substring($bar,$characterPosition,1)"/>
		<xsl:if test="$letter">
			<xsl:choose>
				<xsl:when test="ddue:glossaryEntry[ddue:terms/ddue:term[1]
                  [translate(substring(text(),1,1),$allLowerCaseLetters,concat($allUpperCaseLetters, ' ')) = $letter]]">
					<xsl:call-template name="glossaryLetterBarLink">
						<xsl:with-param name="link" select="concat($sectionPrefix,$letter)"/>
						<xsl:with-param name="name" select="$letter"/>
					</xsl:call-template>
					<xsl:if test="not($characterPosition = string-length($bar))">
						<xsl:text> | </xsl:text>
					</xsl:if>
				</xsl:when>
				<xsl:otherwise>
					<xsl:call-template name="glossaryLetterBarLink">
						<xsl:with-param name="name" select="$letter"/>
					</xsl:call-template>
					<xsl:if test="not($characterPosition = string-length($bar))">
						<xsl:text> | </xsl:text>
					</xsl:if>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:call-template name="glossaryLetterBarLinkRecursive">
				<xsl:with-param name="sectionPrefix" select="$sectionPrefix"/>
				<xsl:with-param name="bar" select="$bar"/>
				<xsl:with-param name="characterPosition" select="$characterPosition + 1"/>
			</xsl:call-template>
		</xsl:if>
	</xsl:template>

	<xsl:template name="glossaryLetterBarLink">
		<xsl:param name="link"/>
		<xsl:param name="name"/>
		<xsl:choose>
			<xsl:when test="$link">
				<a>
					<xsl:attribute name="href">
						#<xsl:value-of select="$link"/>
					</xsl:attribute>
					<xsl:value-of select="$name"/>
				</a>
			</xsl:when>
			<xsl:otherwise>
				<span class="nolink">
					<xsl:value-of select="$name"/>
				</span>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- ============================================================================================
	Pass through a chunk of markup.  This differs from the API markup template in that it must strip
	off the "ddue" namespace.  This will allow build components to add HTML elements to a
	pre-transformed document.  You can also use it in topics to support things that aren't addressed
	by the MAML schema and the Sandcastle transforms.
	============================================================================================= -->

	<xsl:template match="ddue:markup" name="t_ddue_markup">
		<xsl:apply-templates select="node()" mode="markup"/>
	</xsl:template>

	<xsl:template match="*" mode="markup" name="t_ddue_markup_content">
		<xsl:element name="{name()}">
			<xsl:copy-of select="@*"/>
			<xsl:apply-templates select="node()" mode="markup"/>
		</xsl:element>
	</xsl:template>

	<xsl:template match="text() | comment()" mode="markup" name="t_ddue_markup_text">
		<xsl:copy-of select="."/>
	</xsl:template>

</xsl:stylesheet>
