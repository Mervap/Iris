import SwiftUI
import shared
import KMPNativeCoroutinesAsync

struct ContentView: View {
    @ObservedObject var eventsObservable = EventsObservable()
    
    var body: some View {
        List(eventsObservable.events, id: \.title) { event in
            Text("\(event.title) (\(event.description_))")
        }
    }
}

struct ContentView_Previews: PreviewProvider {
	static var previews: some View {
		ContentView()
	}
}
