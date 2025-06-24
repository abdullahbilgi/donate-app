import { MapContainer, TileLayer, Marker, useMapEvents } from "react-leaflet";
import { useEffect, useState } from "react";

interface MarketMapProps {
  setAddress: React.Dispatch<React.SetStateAction<string | null>>;
}
export default function MarketMap({ setAddress }: MarketMapProps) {
  const [position, setPosition] = useState<[number, number] | null>(null);

  useEffect(() => {
    if (!position) return;

    const [lat, lon] = position;
    const apiKey = "pk.6a456cf7c4097a31d0922d814589261f"; // 🔐 Buraya kendi key’ini yaz

    fetch(
      `https://us1.locationiq.com/v1/reverse?key=${apiKey}&lat=${lat}&lon=${lon}&format=json`
    )
      .then((res) => res.json())
      .then((data) => {
        console.log(data);

        setAddress(data);
      })
      .catch((err) => console.error("LocationIQ reverse error:", err));
  }, [position]);

  return (
    <MapContainer
      center={[39.92, 32.85]}
      zoom={6}
      style={{ height: "30vh", width: "100%" }}
    >
      <TileLayer url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png" />
      <ClickHandler
        onSelect={(latlng) => setPosition([latlng.lat, latlng.lng])}
      />
      {position && <Marker position={position} />}
    </MapContainer>
  );
}

function ClickHandler({
  onSelect,
}: {
  onSelect: (latlng: { lat: number; lng: number }) => void;
}) {
  useMapEvents({
    click(e) {
      onSelect(e.latlng);
    },
  });
  return null;
}
