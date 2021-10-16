export default function ({ store, redirect }) {
  // If the user is not authenticated
  if (!store.$auth.loggedIn) {
    return redirect('/')
  }
}
