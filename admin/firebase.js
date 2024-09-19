// Import the functions you need from the SDKs you need
import { getAnalytics } from "firebase/analytics";
import { initializeApp } from "firebase/app";
import getDatabase from "firebase/database";
// TODO: Add SDKs for Firebase products that you want to use
// https://firebase.google.com/docs/web/setup#available-libraries

// Your web app's Firebase configuration
// For Firebase JS SDK v7.20.0 and later, measurementId is optional
const firebaseConfig = {
  apiKey: "AIzaSyCspLsug5m3JKwarri0Jis4mrLRSwIWnHs",
  authDomain: "flexcleanhouse.firebaseapp.com",
  projectId: "flexcleanhouse",
  storageBucket: "flexcleanhouse.appspot.com",
  messagingSenderId: "955476236687",
  appId: "1:955476236687:web:f797e67a8143d8472313fa",
  measurementId: "G-7DRXH2B77T",
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);
const analytics = getAnalytics(app);
const database = getDatabase(app);
