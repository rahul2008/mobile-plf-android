def notify(String channelName, Closure body) {
    try {
        body.call()
    } catch (Exception e) {
        slackSend channel: channelName, color: 'danger', teamDomain: 'philips-cdp2', message: "Build failed: *<${env.BUILD_URL}|${env.BUILD_TAG}>*", token: 'HRTjgsXFuLBJJrCPXXiRGAYz'
        throw e
    }
}
return this