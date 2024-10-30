// Import the functions you need from the SDKs you need
import { getAnalytics } from "firebase/analytics";
import { initializeApp } from "firebase/app";
import { getAuth } from "firebase/auth";
import getDatabase from "firebase/database";
import { getStorage } from "firebase/storage";
// TODO: Add SDKs for Firebase products that you want to use
// https://firebase.google.com/docs/web/setup#available-libraries

// Your web app's Firebase configuration
// For Firebase JS SDK v7.20.0 and later, measurementId is optional
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
const database = getDatabase(app);
const storage = getStorage(app);
export { analytics, app, auth, database, storage };
