// Import the functions you need from the SDKs you need
import { getAnalytics } from "firebase/analytics";
import { initializeApp } from "firebase/app";
import { getAuth } from "firebase/auth";
const firebaseConfig = {
  apiKey: "AIzaSyCspLsug5m3JKwarri0Jis4mrLRSwIWnHs",
  authDomain: "flexcleanhouse.firebaseapp.com",
  databaseURL: "https://flexcleanhouse-default-rtdb.firebaseio.com",
  projectId: "flexcleanhouse",
  storageBucket: "flexcleanhouse.appspot.com",
  messagingSenderId: "955476236687",
  appId: "1:955476236687:web:4f9099599496579e2313fa",
  measurementId: "G-LCNW8T825E",
};
// Initialize Firebase
const app = initializeApp(firebaseConfig);
const analytics = getAnalytics(app);
const auth = getAuth(app);
export { analytics, auth };