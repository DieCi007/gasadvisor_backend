package it.gasadvisor.gas_backend.job.app_update

import org.springframework.stereotype.Component

@Component
class AppUpdateObserver {
    val subscribers: MutableList<AppUpdateSubscriber> = mutableListOf()

    fun register(sub: AppUpdateSubscriber) {
        subscribers.add(sub)
    }

    fun notifyAppUpdate() {
        subscribers.forEach(AppUpdateSubscriber::onAppUpdate)
    }
}
