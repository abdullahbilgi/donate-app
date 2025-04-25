interface LayoutProps {
  children: React.ReactNode;
}

const SignupLoginLayout: React.FC<LayoutProps> = ({ children }) => {
  return (
    <div
      className="flex items-center justify-center w-full h-screen"
      style={{ backgroundColor: "#edf2ec" }}
    >
      <div className="bg-gray-200 px-12 py-10 rounded-2xl shadow-2xl min-h-3/4 flex flex-col justify-center items-center gap-10">
        {children}
      </div>
    </div>
  );
};

export default SignupLoginLayout;
