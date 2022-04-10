<h1>${plugin.name}</h1>
${(plugin.description)!}

<h2 id="usage">Usage</h2>
Include the following snippet in your <tt>pom.xml</tt> file before using the plugin:
<pre class="language-xml"><code>&lt;build&gt;
    &lt;plugins&gt;
        &lt;plugin&gt;
            &lt;groupId&gt;${plugin.groupId}&lt;/groupId&gt;
            &lt;artifactId&gt;${plugin.artifactId}&lt;/artifactId&gt;
            &lt;version&gt;${plugin.version}&lt;/version&gt;
            &lt;configuration&gt;
                ...
            &lt;/configuration&gt;
        &lt;/plugin&gt;
    &lt;/plugins&gt;
&lt;/build&gt;
</code></pre>
<br/>
Below there is a list of available goals provided by this plugin:
<br/>
<table style="font-size: 0.8em">
    <#list iterable(plugin.mojos,'mojo')>
        <tr>
            <th>Goal</th><th>Description</th>
        </tr>
        <#items as mojo>
        <tr>
          <td><a href="#${mojo.goal}"><@mojoGoal mojo/></a></td>
          <td>${(mojo.description)!}</td>
        </tr>
        </#items>
    </#list>
</table>


<h2 id="goals">Goals</h2>





<#list iterable(plugin.mojos,'mojo') as mojo>

    <h3 id="${mojo.goal}"><@mojoGoal mojo/></h3>

    <div class="mojo" style="margin-left: 1rem">
        ${(mojo.description)!""}




    <#list iterable(mojo.parameters,'parameter')>
        <h4>Parameters</h4>
        <div class="mojo-parameters" style="margin-left: 1rem">
        <#items as parameter>
            <#if parameter.editable == 'false'><#continue></#if>
            <h5 style="border-bottom: 1px solid;">${parameter.name}</h5>
            ${parameter.description}
            <ul style="font-size: 0.8em">
                <li><b>Type:</b> <tt>${parameter.type}</tt></li>
                <li><b>Required:</b> ${parameter.required}</li>
                <@searchDefaultValue mojo parameter/>
                <@searchProperty mojo parameter/>
                <#if parameter.since??><li><b>Since:</b> ${parameter.since}</li></#if>
            </ul>
        </#items>
        </div>
    </#list>


        <h4>Mojo Details</h4>
        <div class="mojo-details" style="margin-left: 1rem">
            <table style="font-size: 0.8em">
                <#list mojo as key, value >
                    <#if !(mojo[key]?is_hash) && !(mojo[key]?is_sequence) >
                        <tr><td><b>${key}</b></td><td>${value}</td></tr>
                    </#if>
                </#list>
            </table>
        </div>

    </div>

</#list>


<#macro searchDefaultValue mojo parameter>
    <#if mojo.configuration?? &&  mojo.configuration[parameter.name]?? >
        <#list mojo.configuration[parameter.name] as key,value>
            <#if key == 'default-value'>
                <li><b>Default value:</b> <tt>${value}</tt></li>
            </#if>
        </#list>
    </#if>
</#macro>

<#macro searchProperty mojo parameter>
    <#if mojo.configuration?? &&  mojo.configuration[parameter.name]?? >
        <#list mojo.configuration[parameter.name] as key,value>
            <#if key == 'value'>
                <li><b>User property:</b> <tt>${value?replace("${","")?replace("}","")}</tt></li>
            </#if>
        </#list>
    </#if>
</#macro>


<#macro mojoGoal mojo>
    <#if plugin.goalPrefix??>
        ${plugin.goalPrefix}:${mojo.goal}
    <#else>
        ${mojo.goal}
    </#if>
</#macro>


<#function iterable values mapKey>
    <#if values?is_enumerable>
        <#return values />
    <#elseif values?is_hash>
        <#if values[mapKey]?? >
           <#return [ values[mapKey] ]  />
        <#else>
           <#return [ ]  />
        </#if>
    </#if>
</#function>