import { FaArrowDown } from "react-icons/fa";

const DonateCellProduct = () => {
  return (
    <div className="flex flex-col">
      <div className="relative bg-[url('/images/donation.jpg')] bg-cover bg-center h-[70vh]">
        <div className="absolute inset-0 bg-black/40"></div>
        <div className="absolute p-20 text-center text-gray-200">
          <h1 className="text-4xl font-bold mb-10">
            Seninle Büyüyen Bir İyilik Hareketi
          </h1>
          <p className="text-xl font-medium  tracking-wider leading-relaxed mb-13">
            Tarihi yaklaşan ürünlerini veya tüketemeyecegin ürünlerini
            paylaşarak hem ihtiyacı olanlara destek ol, hem de israfı önle.
            Seninle birlikte bu zinciri büyütelim. Her yeni ürün, her paylaşım,
            her katkı bizi biraz daha büyütüyor.
            <strong>Sen de bu topluluğun bir parçası ol.</strong> Bugün bir
            paylaşım senden, yarın binlerce iyilik hepimizden.
          </p>
          <p className="text-xl font-medium  tracking-wider leading-relaxed">
            LastBite'i daha yakından tanımak ister misiniz? Biz, ihtiyaç
            sahipleriyle fazla ürünleri buluşturarak israfı önlemeyi ve
            toplumsal dayanışmayı büyütmeyi hedefliyoruz. Misyonumuz,
            değerlerimiz ve diger tüm sürecler hakkında daha fazla bilgi almak
            için lütfen Hakkımızda sayfamıza göz atın!
          </p>
        </div>

        <div className="flex items-center gap-2 absolute -bottom-8 left-1/2 -translate-x-1/2 bg-lime-300 text-teal-800 font-semibold text-xl py-3 px-9 rounded-md ">
          Donation Form <FaArrowDown />
        </div>
      </div>

      <div className="">
        <h1>deneme</h1>
      </div>
    </div>
  );
};

export default DonateCellProduct;
