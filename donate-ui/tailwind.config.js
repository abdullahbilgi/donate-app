/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./index.html", "./src/**/*.{js,ts,jsx,tsx}"],
  theme: {
    extend: {
      fontFamily: {
        poppins: ["Poppins", "sans-serif"],
      },
      fontSize: { "10xl": "12rem" },
      colors: {
        mainButton: "#12345",
      },
      spacing: {
        header: "26px",
      },
    },
  },
  plugins: [],
};
