import Foundation
import shared
import KMPNativeCoroutinesAsync

@MainActor
class EventsObservable: ObservableObject {
    @Published var events = [Event]()
    private var eventsTask: Task<(), Never>? = nil
    
    init() {
        eventsTask = Task {
            do {
                let stream = asyncStream(for: DB().selectAllEventsNative())
                for try await data in stream {
                    self.events = data
                }
            } catch {
                print("Failed with error: \(error)")
            }
        }
    }
}
