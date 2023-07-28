import ArrivalDate from './arrivalDate.js';

import {useLocation} from 'react-router-dom';

const pdpStyle = {
    display: 'grid',
    gridTemplateColumns: '1fr 1fr',
    width: '70%',
    gridGap: '20px',
    padding: '20px',
    border: '2px solid black',
    borderRadius: '10px',
    boxShadow: '5px 5px 5px 5px #888888',
    position : 'absolute',
    top: '50%',
    left: '50%',
    transform: 'translate(-50%, -50%)'
}

const imgStyle = {
    width: '100%',
    height: '100%',
    borderRadius: '10px',
    objectFit: 'cover'
}

function Details(){

    const location = useLocation();
    
    const { sku, vendor, shipping_method , prod_image} = location.state;
    let prop = location.state;

    return (
        <div className='PDP' style = {pdpStyle}>
            <div className='LeftSide'>
                <img src={prod_image} alt="product" style = {imgStyle}/>
            </div>
            <div className='RightSide'>
            <h1>Details</h1>
            <p>SKU: {sku}</p>
            <p>Vendor: {vendor}</p>
            <p>Shipping Method: {shipping_method}</p>
            <ArrivalDate sku={sku} vendor={vendor} shipping_method={shipping_method}/>
            </div>
        </div>
    )

}

export default Details;