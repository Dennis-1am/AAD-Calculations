import './App.css';
import Navbar from './components/Navbar.js';
import Product from './components/Product.js';
import images from './images/Diamond.png';
import Details from './components/Details.js';

import {Routes, Route } from "react-router-dom";


const layoutGrid = {
  display: 'grid',
  gridTemplateColumns: '1fr 1fr 1fr',
  width: '1025px',
  margin: '10rem auto',
  border: '2px solid black',
  borderRadius: '10px',
}

function App() {
  return (
    <div className="app">
      <Routes>
        <Route path="*" element={
          <>
            <Navbar />
            <div style={layoutGrid}>
              <Product image={images} sku="20343601" vendor="1304021" shipping_method="Next Day Air" name="Diamond" price="$1000"/>
              <Product image={images} sku="20343601" vendor="0006256" shipping_method="Next Day Air"  name="Diamond" price="$1000" />
              <Product image={images} sku="20343601" vendor="706618000" shipping_method="Ground"  name="Diamond" price="$2000"/>
              <Product image={images} sku="20146170" vendor="706618000" shipping_method="Ground"  name="Diamond" price="$3000"/>
              <Product image={images} sku="20146170" vendor="SIGNET" shipping_method="2nd Business Day"  name="Diamond" price="$4000"/>
              <Product image={images} sku="423234703" vendor="1304021" shipping_method="2nd Business Day"  name="Diamond" price="$5000"/>
            </div>
          </>}>
        </Route>
        <Route path="details" element={<Details />}></Route>
      </Routes>
    </div>
  );
}

export default App;
