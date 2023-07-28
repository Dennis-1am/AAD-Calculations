import './Product.css'
import { Outlet, Link } from "react-router-dom";

function Product(prop) {
    return (
        <>
        <div className="Product">
            <Link to="details"
                state={{
                    sku: prop.sku,
                    vendor: prop.vendor,
                    shipping_method: prop.shipping_method,
                    prod_image: prop.image
                }}>
                <div>
                    <img src={prop.image} alt={prop.name} />
                </div>
            </Link>
            <Outlet />
            <div>
                <h3>{prop.name}</h3>
                <p>{prop.price}</p>
            </div>
        </div>
        </>
    );
}

export default Product;