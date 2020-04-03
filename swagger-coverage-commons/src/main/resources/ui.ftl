<#macro progressbar current full height=16>
    <#if full gt 0 >
        <#assign percentValue = 100*current/(full)>
        <#assign bgStyle = "bg-danger">

        <#if percentValue gt 33>
            <#assign bgStyle = "bg-warning">
        </#if>

        <#if percentValue gt 66>
            <#assign bgStyle = "bg-success">
        </#if>

        <div class="progress" style="height: ${height}px;">
            <div
                    class="progress-bar ${bgStyle}" role="progressbar"
                    style="width: ${percentValue}%"
                    aria-valuenow="${current}"
                    aria-valuemin="0"
                    aria-valuemax="${full}">
            </div>

        </div>
    </#if>
</#macro>

<#macro progress current full postfix>
    <div class="row">
        <div class="col-12">
            <#if full gt 0 >
                ${current}/${full}
                (${100*(current/full)}%)
            <#else>
                --
            </#if>
            ${postfix}
        </div>
    </div>
    <div class="row">
        <div class="col-12">
            <#if full gt 0 >
                <@progressbar current = current full = full />
            </#if>
        </div>
    </div>
</#macro>

<#macro coverageBadget counter legend>
    <#if counter.all gt 0>
        <div class="card" style="margin-bottom: 16px;">
            <div class="card-header">
                ${legend}
            </div>
            <div class="card-body">
                <div class="row">
                    <div class="col-sm">
                        <div class="alert alert-success" role="alert">
                            ${i18["badged.full"]}: ${counter.full * 100 / counter.all}%
                        </div>
                    </div>
                    <div class="col-sm">
                        <div class="alert alert-warning" role="alert">
                            ${i18["badged.partial"]}: ${counter.party * 100 / counter.all}%
                        </div>
                    </div>
                    <div class="col-sm">
                        <div class="alert alert-danger" role="alert">
                            ${i18["badged.empty"]}: ${counter.empty * 100 / counter.all}%
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-12">
                        <div class="progress" style="height: 30px;">
                            <div
                                    class="progress-bar bg-success progress-bar-striped" role="progressbar"
                                    style="width: ${counter.full * 100 / counter.all}%"
                                    aria-valuenow="${counter.full}"
                                    aria-valuemin="0"
                                    aria-valuemax="${counter.all}">
                            </div>
                            <div
                                    class="progress-bar bg-warning progress-bar-striped" role="progressbar"
                                    style="width: ${counter.party * 100 / counter.all}%"
                                    aria-valuenow="${counter.party}"
                                    aria-valuemin="0"
                                    aria-valuemax="${counter.all}">
                            </div>
                            <div
                                    class="progress-bar bg-danger progress-bar-striped" role="progressbar"
                                    style="width: ${counter.empty * 100 / counter.all}%"
                                    aria-valuenow="${counter.empty}"
                                    aria-valuemin="0"
                                    aria-valuemax="${counter.all}">
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </#if>
</#macro>

<#macro coverageStateBadget operationResult>
    <#if operationResult.processCount == 0>
        <span class="badge badge-dark">${i18["common.state.no_call"]}</span>
    <#else>
        <#switch operationResult.state>
            <#case "FULL">
                <span class="badge badge-success">${i18["common.state.full"]}</span>
                <#break>
            <#case "PARTY">
                <span class="badge badge-warning">${i18["common.state.partial"]}</span>
                <#break>
            <#case "EMPTY">
                <span class="badge badge-danger">${i18["common.state.empty"]}</span>
                <#break>
            <#default>
        </#switch>
    </#if>
</#macro>